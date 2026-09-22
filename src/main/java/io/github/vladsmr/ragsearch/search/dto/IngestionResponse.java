package io.github.vladsmr.ragsearch.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class IngestionResponse {

    private int chunks;
    private int status;

}
