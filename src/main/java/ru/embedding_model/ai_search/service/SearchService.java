package ru.embedding_model.ai_search.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import ru.embedding_model.ai_search.dto.OsResponseDto;

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
        final OsResponseDto osResponse = similaritySearch(searchRequest);
        if (osResponse.isHasError()) {
            return osResponse.getErrorMessage();
        }
        final List<Document> chunks = osResponse.getDocuments();
        return ollamaService.search(chunks, query);
    }

    public OsResponseDto search(final String query) {
        final SearchRequest searchRequest = getRequest(query);
        return similaritySearch(searchRequest);
    }

    private OsResponseDto similaritySearch(final SearchRequest request) {
        try {
            final List<Document> chunks = vectorStore.similaritySearch(request);
            final OsResponseDto response = new OsResponseDto();
            response.setDocuments(chunks);
            return response;
        } catch (final RuntimeException e) {
            final OsResponseDto response = new OsResponseDto();
            response.setErrorMessage(String.format("error calling open search: %s", e.getMessage()));
            response.setHasError(true);
            return response;
        }
    }

}
