package ru.embedding_model.ai_search.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.ai.document.Document;

@Getter
@Setter
public class OsResponseDto {

    private List<Document> documents;
    private String errorMessage;
    private boolean hasError;

}
