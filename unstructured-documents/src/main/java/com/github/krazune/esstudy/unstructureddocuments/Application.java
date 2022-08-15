package com.github.krazune.esstudy.unstructureddocuments;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.github.krazune.esstudy.unstructureddocuments.random.RandomDocumentObject;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Application
{
	protected static final String ES_HOST = "localhost";
	protected static final int ES_PORT = 9200;
	protected static final String INDEX_NAME = "unstructored_documents";
	protected static final int RANDOM_DOCUMENT_INDICES = 10;
	protected static final String DELETE_DOCUMENT_ID = "3";
	protected static final int MATCH_ALL_LIMIT = 2;
	protected static final int MATCH_ALL_PAGES = 5;

	public static void main(String[] args) throws IOException, ExecutionException, InterruptedException
	{
		try (RestClientTransport transport = createTransport())
		{
			createIndex(transport);
			indexDocuments(transport);
			deleteDocument(transport);
			queryMatchAllPaginated(transport);
		}
	}

	private static void deleteDocument(RestClientTransport transport) throws IOException
	{
		ElasticsearchClient esClient = new ElasticsearchClient(transport);

		System.out.println("Deleting document with id " + DELETE_DOCUMENT_ID + '.');

		DeleteResponse deleteResponse = esClient.delete(
			drb -> drb
				.index(INDEX_NAME)
				.id(DELETE_DOCUMENT_ID)
				.refresh(Refresh.True) // Refreshes the affected shards to make this operation visible to search. WaitFor would take too long to index all documents.
		);

		System.out.println("Response:" + deleteResponse.toString());

		System.out.println();
	}

	protected static RestClientTransport createTransport()
	{
		return new RestClientTransport(
			RestClient.builder(
				new HttpHost(ES_HOST, ES_PORT)
			).build(),
			new JacksonJsonpMapper()
		);
	}

	protected static void createIndex(ElasticsearchTransport transport) throws IOException
	{
		ElasticsearchIndicesClient indicesClient = new ElasticsearchIndicesClient(transport);

		try
		{
			System.out.println("Creating the " + INDEX_NAME + " index.");

			CreateIndexResponse createIndexResponse = indicesClient.create(
				cirb -> cirb
					.index(INDEX_NAME)
			);

			System.out.println("Index " + INDEX_NAME + " created.");
			System.out.println("Response: " + createIndexResponse + '\n');
		}
		catch (ElasticsearchException e)
		{
			String errorType = e.error().type();

			switch (errorType)
			{
				case "resource_already_exists_exception":
					System.out.println("Index " + INDEX_NAME + " already exists.\n");
					break;

				default:
					throw new RuntimeException(e);
			}
		}
	}

	protected static void indexDocuments(RestClientTransport transport) throws IOException, ExecutionException, InterruptedException
	{
		ElasticsearchAsyncClient esAsyncClient = new ElasticsearchAsyncClient(transport);
		List<CompletableFuture<IndexResponse>> responseFutures = new LinkedList<>();

		System.out.println("Indexing " + RANDOM_DOCUMENT_INDICES + " documents.");

		for (int i = 0; i < RANDOM_DOCUMENT_INDICES; ++i)
		{
			final String id = Integer.toString(i);

			responseFutures.add(
				esAsyncClient.index(
					irb -> irb
						.index(INDEX_NAME)
						.refresh(Refresh.True) // Refreshes the affected shards to make this operation visible to search. WaitFor would take too long to index all documents.
						.id(id)
						.document(RandomDocumentObject.getDocumentObject())
				)
			);

		}

		for (int i = 0; i < responseFutures.size(); ++i)
		{
			System.out.println("Response " + i + ": " + responseFutures.get(0).get());
		}

		System.out.println("All documents indexed.\n");
	}

	protected static void queryMatchAllPaginated(RestClientTransport transport) throws IOException
	{
		ElasticsearchClient esClient = new ElasticsearchClient(transport);

		System.out.println("Matching all " + MATCH_ALL_PAGES + " first pages, " + MATCH_ALL_LIMIT + " documents per page.");

		for (int i = 0; i < MATCH_ALL_PAGES; ++i)
		{
			final int from = i * MATCH_ALL_LIMIT;

			System.out.println("Page " + i);

			SearchResponse<Object> matchAllDocuments = esClient.search(
				srb -> srb
					.index(INDEX_NAME)
					.from(from)
					.size(MATCH_ALL_LIMIT)
					.query(
						qb -> qb
							.matchAll(maqb -> maqb)
					),
				Object.class
			);

			for (Hit<Object> hit : matchAllDocuments.hits().hits())
			{
				System.out.println("Document id: " + hit.id());
				System.out.println(hit.source());
			}
		}

		System.out.println();
	}
}
