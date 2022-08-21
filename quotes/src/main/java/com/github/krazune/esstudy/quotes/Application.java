package com.github.krazune.esstudy.quotes;

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
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.github.krazune.esstudy.helper.EsHelper;
import com.github.krazune.esstudy.quotes.document.Quote;
import com.github.krazune.esstudy.quotes.random.RandomQuoteGenerator;
import com.github.krazune.esstudy.random.text.RandomWordGenerator;

import java.io.IOException;

public class Application
{
	private static final String INDEX_NAME = "quotes";
	private static final int RANDOM_DOCUMENT_INDICES = 10;
	private static final int MATCH_ALL_SIZE = 10;
	private static final int MATCH_SIZE = 5;

	public static void main(String[] args) throws IOException
	{
		try (RestClientTransport transport = EsHelper.createTransport())
		{
			createIndex(transport);
			indexDocuments(transport);
			queryMatchAll(transport);
			queryMatch(transport);
		}
	}

	private static TypeMapping createTypeMapping()
	{
		return new TypeMapping.Builder()
			.dynamic(DynamicMapping.Strict)
			.properties("quote", pb -> pb.text(tpb -> tpb)) // yarr
			.properties("author", pb -> pb.text(tpb -> tpb))
			.build();
	}

	private static void createIndex(ElasticsearchTransport transport) throws IOException
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

	private static void indexDocuments(RestClientTransport transport) throws IOException
	{
		ElasticsearchClient esClient = new ElasticsearchClient(transport);

		System.out.println("Indexing " + RANDOM_DOCUMENT_INDICES + " documents.");

		for (int i = 0; i < RANDOM_DOCUMENT_INDICES; ++i)
		{
			IndexResponse indexResponse = esClient.index(
				irb -> irb
					.index(INDEX_NAME)
					.refresh(Refresh.True)
					.document(RandomQuoteGenerator.getQuote())
			);

			System.out.println("Document " + i + " response: " + indexResponse);
		}

		System.out.println("All documents indexed.\n");
	}

	private static void queryMatchAll(RestClientTransport transport) throws IOException
	{
		ElasticsearchClient esClient = new ElasticsearchClient(transport);

		System.out.println("Matching all " + MATCH_ALL_SIZE + " first documents.");

		SearchResponse<Quote> matchAllDocuments = esClient.search(
			srb -> srb
				.index(INDEX_NAME)
				.size(MATCH_ALL_SIZE)
				.query(
					qb -> qb
						.matchAll(maqb -> maqb)
				),
			Quote.class
		);

		for (Hit<Quote> hit : matchAllDocuments.hits().hits())
		{
			System.out.println(hit.source());
		}

		System.out.println();
	}

	private static void queryMatch(RestClientTransport transport) throws IOException
	{
		ElasticsearchClient esClient = new ElasticsearchClient(transport);
		String matchWord = RandomWordGenerator.getWord();
		System.out.println("Matching all " + MATCH_SIZE + " first documents, with a quote that matches the word \"" + matchWord + "\".");

		SearchResponse<Quote> matchDocuments = esClient.search(
			srb -> srb
				.index(INDEX_NAME)
				.size(MATCH_SIZE)
				.explain(true)
				.query(
					qb -> qb
						.match(
							mqb -> mqb
								.field("quote")
								.query(matchWord)
						)
				),
			Quote.class
		);

		for (Hit<Quote> hit : matchDocuments.hits().hits())
		{
			System.out.println(hit.source());
			System.out.println("Score: " + hit.score());
			System.out.println(hit.explanation());
			System.out.println();
		}

		System.out.println();
	}
}
