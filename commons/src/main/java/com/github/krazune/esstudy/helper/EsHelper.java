package com.github.krazune.esstudy.helper;

import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

public class EsHelper
{
	private static final String HOST = "localhost";
	private static final int PORT = 9200;

	public static RestClientTransport createTransport()
	{
		return new RestClientTransport(
			RestClient.builder(
				new HttpHost(HOST, PORT)
			).build(),
			new JacksonJsonpMapper()
		);
	}
}
