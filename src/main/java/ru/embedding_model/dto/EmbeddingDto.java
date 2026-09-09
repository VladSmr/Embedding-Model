package ru.embedding_model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmbeddingDto {

    private float[] embedding;
    private String word;

}
