package io.github.vladsmr.ragsearch.config;

import org.apache.hc.core5.http.HttpHost;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.httpclient5.ApacheHttpClient5TransportBuilder;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.opensearch.OpenSearchVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenSearchConfig {

    @Bean
    public OpenSearchClient openSearchClient(@Value("${opensearch.host}") final String host,
                                             @Value("${opensearch.port}") final String port) {
        final HttpHost httpHost = new HttpHost("http", host, Integer.parseInt(port));
        OpenSearchTransport transport = ApacheHttpClient5TransportBuilder.builder(httpHost).build();
        return new OpenSearchClient(transport);
    }

    @Bean
    public VectorStore vectorStore(final OpenSearchClient openSearchClient,
                                   final EmbeddingModel embeddingModel,
                                   @Value("${opensearch.index-name}") final String indexName) {
        return OpenSearchVectorStore.builder(openSearchClient, embeddingModel)
                                    .index(indexName)
                                    .initializeSchema(true)
                                    .dimensions(1024)
                                    .build();
    }

}