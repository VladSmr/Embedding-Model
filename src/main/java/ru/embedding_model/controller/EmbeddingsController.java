package ru.embedding_model.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.embedding_model.dto.CosineDto;
import ru.embedding_model.dto.EmbeddingDto;
import ru.embedding_model.service.CosineService;
import ru.embedding_model.service.VectorService;

@RestController
@RequestMapping("/todo")
@AllArgsConstructor
public class EmbeddingsController {

    private final CosineService cosineService;
    private final VectorService vectorService;

    @PostMapping("/cosine")
    public ResponseEntity<CosineDto> cosine(@RequestBody CosineDto word) {
        final CosineDto response = cosineService.calculate(word);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/vector")
    public ResponseEntity<EmbeddingDto> vector(@RequestBody EmbeddingDto word) {
        final EmbeddingDto response = vectorService.generate(word);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
