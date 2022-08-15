package com.github.krazune.esstudy.products;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.mapping.DynamicMapping;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.elasticsearch.indices.IndexSettingsAnalysis;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.github.krazune.esstudy.products.product.Product;
import com.github.krazune.esstudy.products.random.product.RandomProductGenerator;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Application
{
	protected static final String ES_HOST = "localhost";
	protected static final int ES_PORT = 9200;
	protected static final String INDEX_NAME = "products";
	protected static final int RANDOM_DOCUMENT_INDICES = 30;
	protected static final String CUSTOM_SYNONYMS_ANALYZER_NAME = "my_synonym_analyzer";
	protected static final String CUSTOM_SYNONYMS_FILTER_NAME = "my_synonym_filter";
	protected static final List<String> SYNONYMS = List.of(
		"alone, solo",
		"have, possess"
	);
	protected static final String MATCH_QUERY = "solo possess";
	protected static final int MATCH_QUERY_LIMIT = 2;
	protected static final int RANGE_QUERY_MINIMUM = 650;
	protected static final int RANGE_QUERY_MAXIMUM = 775;
	protected static final String TAG_SALES_TERMS_AGGREGATION_NAME = "my_tag_sales_terms_aggregation";
	protected static final String TAG_SALES_STATS_AGGREGATION_NAME = "my_tag_sales_stats_aggregation";

	public static void main(String[] args) throws IOException
	{
		try (RestClientTransport transport = createTransport())
		{
			createIndex(transport);
			indexDocuments(transport);
			queryMatch(transport);
			rangeMatch(transport);
			tagSalesStatsAggregationSearch(transport);
		}
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

	protected static TypeMapping createTypeMapping()
	{
		return new TypeMapping.Builder()
			.dynamic(DynamicMapping.Strict)
			.properties("name", pb -> pb.text(tpb -> tpb.analyzer(CUSTOM_SYNONYMS_ANALYZER_NAME)))
			.properties("description", pb -> pb.text(tpb -> tpb.analyzer(CUSTOM_SYNONYMS_ANALYZER_NAME)))
			.properties(
				"sales",
				pb -> pb.integer(
					inp -> inp
						.coerce(false)
				)
			)
			.properties(
				"tags",
				pb0 -> pb0.keyword(kpb -> kpb)
			)
			.build();
	}

	protected static IndexSettingsAnalysis createAnalysisSettings()
	{
		return new IndexSettingsAnalysis.Builder()
			.analyzer(
				CUSTOM_SYNONYMS_ANALYZER_NAME,
				ab -> ab
					.custom(
						cab -> cab
							.tokenizer("standard")
							.filter("lowercase", CUSTOM_SYNONYMS_FILTER_NAME)
					)
			)
			.filter(
				CUSTOM_SYNONYMS_FILTER_NAME,
				tkb -> tkb
					.definition(
						tfdb -> tfdb
							.synonym(
								stfb -> stfb
									.synonyms(SYNONYMS)
							)
					)
			)
			.build();
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
					.settings(
						isb -> isb
							.analysis(createAnalysisSettings())
					)
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
					.document(RandomProductGenerator.getProduct())
			);

			System.out.println("Document " + i + " response: " + indexResponse);
		}

		System.out.println("All documents indexed.\n");
	}

	protected static void queryMatch(RestClientTransport transport) throws IOException
	{
		ElasticsearchClient esClient = new ElasticsearchClient(transport);
		System.out.println("Matching all " + MATCH_QUERY_LIMIT + " first documents, with a quote that matches the search \"" + MATCH_QUERY + "\".");

		SearchResponse<Product> matchDocuments = esClient.search(
			srb -> srb
				.index(INDEX_NAME)
				.size(MATCH_QUERY_LIMIT)
				.explain(true)
				.query(
					qb -> qb
						.match(
							mqb -> mqb
								.field("description")
								.query(MATCH_QUERY)
						)
				),
			Product.class
		);

		for (Hit<Product> hit : matchDocuments.hits().hits())
		{
			System.out.println(hit.source());
			System.out.println("Score: " + hit.score());
			System.out.println(hit.explanation());
			System.out.println();
		}

		System.out.println();
	}

	private static void rangeMatch(RestClientTransport transport) throws IOException
	{
		ElasticsearchClient esClient = new ElasticsearchClient(transport);
		System.out.println("Matching products with sales between " + RANGE_QUERY_MINIMUM + " and " + RANGE_QUERY_MAXIMUM + '.');

		SearchResponse<Product> matchDocuments = esClient.search(
			srb -> srb
				.index(INDEX_NAME)
				.explain(true)
				.query(
					qb -> qb
						.range(
							rqb -> rqb
								.field("sales")
								.gte(JsonData.of(RANGE_QUERY_MINIMUM))
								.lte(JsonData.of(RANGE_QUERY_MAXIMUM))
						)
				),
			Product.class
		);

		for (Hit<Product> hit : matchDocuments.hits().hits())
		{
			System.out.println(hit.source());
			System.out.println(hit.explanation());
			System.out.println();
		}

		System.out.println();
	}

	private static void tagSalesStatsAggregationSearch(RestClientTransport transport) throws IOException
	{
		ElasticsearchClient esClient = new ElasticsearchClient(transport);
		System.out.println("Stats of sales per tag:");

		SearchResponse<Product> matchDocuments = esClient.search(
			srb -> srb
				.index(INDEX_NAME)
				.size(0)
				.explain(true)
				.aggregations(
					TAG_SALES_TERMS_AGGREGATION_NAME,
					ab0 -> ab0
						.terms(
							tab -> tab
								.field("tags")
						)
						.aggregations(
							TAG_SALES_STATS_AGGREGATION_NAME,
							ab1 -> ab1
								.stats(
									sab -> sab
										.field("sales")
								)
						)
				),
			Product.class
		);

		Aggregate rootAggregate = matchDocuments.aggregations().get(TAG_SALES_TERMS_AGGREGATION_NAME);

		for (StringTermsBucket bucket : rootAggregate.sterms().buckets().array())
		{
			System.out.println("Aggregation key: " + bucket.key());
			System.out.println("Aggregation value: " + bucket);
			System.out.println();

		}

		System.out.println();
	}
}
