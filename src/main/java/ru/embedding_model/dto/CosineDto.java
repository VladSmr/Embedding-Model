package ru.embedding_model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CosineDto {

    private String error;
    private float proximity;
    private String wordOne;
    private String wordTwo;

}
