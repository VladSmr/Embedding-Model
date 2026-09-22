package io.github.vladsmr.ragsearch.embedding.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.vladsmr.ragsearch.embedding.dto.CosineDto;
import io.github.vladsmr.ragsearch.embedding.dto.EmbeddingDto;
import io.github.vladsmr.ragsearch.embedding.service.CosineService;
import io.github.vladsmr.ragsearch.embedding.service.EmbeddingService;

@RestController
@RequestMapping("/embedding")
@AllArgsConstructor
public class EmbeddingController {

    private final CosineService cosineService;
    private final EmbeddingService embeddingService;

    @PostMapping("/cosine")
    public ResponseEntity<CosineDto> cosine(@RequestBody CosineDto word) {
        final CosineDto response = cosineService.calculate(word);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/vector")
    public ResponseEntity<EmbeddingDto> vector(@RequestBody EmbeddingDto word) {
        final EmbeddingDto response = embeddingService.generate(word);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
