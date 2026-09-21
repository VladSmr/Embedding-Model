package ru.embedding_model.config;

import org.apache.hc.core5.http.HttpHost;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.httpclient5.ApacheHttpClient5TransportBuilder;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.opensearch.OpenSearchVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenSearchConfig {

    @Bean
    public OpenSearchClient openSearchClient() {
        HttpHost host = new HttpHost("http", "opensearch", 9200); // docker
        //HttpHost host = new HttpHost("http", "localhost", 9200); // local deploy
        OpenSearchTransport transport = ApacheHttpClient5TransportBuilder.builder(host).build();
        return new OpenSearchClient(transport);
    }

    @Bean
    public VectorStore vectorStore(final OpenSearchClient openSearchClient, final EmbeddingModel embeddingModel) {
        return OpenSearchVectorStore.builder(openSearchClient, embeddingModel)
                                    .index("my-index")
                                    .initializeSchema(true)
                                    .dimensions(1024)
                                    .build();
    }

}