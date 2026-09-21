package ru.embedding_model.ai_search.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SearchService {

    private final OllamaService ollamaService;
    private final VectorStore vectorStore;

    private SearchRequest getRequest(final String query) {
        return SearchRequest.builder()
                            .query(query)
                            .topK(3) // топ 3 чанка
                            .build();
    }

    public String llmSearch(final String query) {
        final SearchRequest searchRequest = getRequest(query);
        final List<Document> chunks = vectorStore.similaritySearch(searchRequest);
        return ollamaService.search(chunks, query);
    }

    public List<Document> search(final String query) {
        final SearchRequest searchRequest = getRequest(query);
        return vectorStore.similaritySearch(searchRequest);
    }

}
