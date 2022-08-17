package com.github.krazune.esstudy.tasks;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.mapping.DynamicMapping;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.github.krazune.esstudy.tasks.document.Task;
import com.github.krazune.esstudy.tasks.random.RandomTaskGenerator;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class Application
{
	protected static final String ES_HOST = "localhost";
	protected static final int ES_PORT = 9200;
	protected static final String INDEX_NAME = "tasks";
	protected static final int RANDOM_DOCUMENT_INDICES = 100;
	protected static final String GOAL_QUERY_TERM = "good";
	protected static final String ACTION_QUERY_TERM = "bad";
	protected static final int NESTED_QUERY_LIMIT = 5;

	public static void main(String[] args) throws IOException
	{
		try (RestClientTransport transport = createTransport())
		{
			createIndex(transport);
			indexDocuments(transport);
			nestedQuery(transport);
		}
	}

	protected static TypeMapping createTypeMapping()
	{
		return new TypeMapping.Builder()
			.dynamic(DynamicMapping.Strict)
			.properties("goal", pb0 -> pb0.text(tpb -> tpb))
			.properties(
				"actions",
				pb -> pb.nested(
					npb -> npb
						.properties("description", pb1 -> pb1.text(tpb -> tpb))
						.properties("complete", pb1 -> pb1.boolean_(bpb -> bpb))
				)
			)
			.build();
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
					.mappings(createTypeMapping())
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

	protected static void indexDocuments(RestClientTransport transport) throws IOException
	{
		ElasticsearchClient esClient = new ElasticsearchClient(transport);

		System.out.println("Indexing " + RANDOM_DOCUMENT_INDICES + " documents.");

		for (int i = 0; i < RANDOM_DOCUMENT_INDICES; ++i)
		{
			IndexResponse indexResponse = esClient.index(
				irb -> irb
					.index(INDEX_NAME)
					.refresh(Refresh.True) // Refreshes the affected shards to make this operation visible to search. WaitFor would take too long to index all documents.
					.document(RandomTaskGenerator.getTask())
			);

			System.out.println("Document " + i + " response: " + indexResponse);
		}

		System.out.println("All documents indexed.\n");
	}

	private static void nestedQuery(RestClientTransport transport) throws IOException
	{
		ElasticsearchClient esClient = new ElasticsearchClient(transport);

		System.out.println("Matching all " + NESTED_QUERY_LIMIT + " first documents.");

		SearchResponse<Task> matchAllDocuments = esClient.search(
			srb -> srb
				.index(INDEX_NAME)
				.size(NESTED_QUERY_LIMIT)
				.query(
					qb0 -> qb0
						.bool(
							bqb -> bqb
								.must(
									qb1 -> qb1
										.match(
											mqb -> mqb
												.field("goal")
												.query(GOAL_QUERY_TERM)
										)
								)
								.must(
									qb1 -> qb1
										.nested(
											nqb -> nqb
												.path("actions")
												.query(
													qb2 -> qb2
														.match(
															mqb -> mqb
																.field("actions.description")
																.query(ACTION_QUERY_TERM)
														)
												)
										)
								)
						)
				),
			Task.class
		);

		for (Hit<Task> hit : matchAllDocuments.hits().hits())
		{
			System.out.println(hit.source());
			System.out.println();
		}

		System.out.println();
	}
}
