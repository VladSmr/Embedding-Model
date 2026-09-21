package ru.embedding_model.ai_embedding.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmbeddingDto {

    private float[] embedding;
    private String word;

}
