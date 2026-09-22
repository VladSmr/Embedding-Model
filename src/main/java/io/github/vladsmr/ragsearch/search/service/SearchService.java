package io.github.vladsmr.ragsearch.search.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import io.github.vladsmr.ragsearch.common.exceptions.OpenSearchUnavailableException;

@Service
@AllArgsConstructor
public class SearchService {

    private final LlmService llmService;
    private final VectorStore vectorStore;

    private SearchRequest getRequest(final String query) {
        return SearchRequest.builder()
                            .query(query)
                            .topK(3) // top 3 chunks
                            .similarityThreshold(0.50) // minimum similarity
                            .build();
    }

    public String llmSearch(final String query) {
        final SearchRequest searchRequest = getRequest(query);
        final List<Document> chunks = similaritySearch(searchRequest);
        return llmService.search(chunks, query);
    }

    public List<Document> search(final String query) {
        final SearchRequest searchRequest = getRequest(query);
        return similaritySearch(searchRequest);
    }

    private List<Document> similaritySearch(final SearchRequest request) {
        try {
            return vectorStore.similaritySearch(request);
        } catch (final RuntimeException e) {
            throw new OpenSearchUnavailableException(String.format("error calling open search: %s", e.getMessage()));
        }
    }

}
