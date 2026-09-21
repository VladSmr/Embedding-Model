package ru.embedding_model.ai_search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseDto {

    private int chunks;
    private int status;

}
