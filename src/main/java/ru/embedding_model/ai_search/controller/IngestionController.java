package ru.embedding_model.ai_search.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.embedding_model.ai_search.dto.DocumentDto;
import ru.embedding_model.ai_search.dto.ResponseDto;
import ru.embedding_model.ai_search.service.IngestionService;

@RestController
@RequestMapping("/ingest")
@AllArgsConstructor
public class IngestionController {

    private final IngestionService service;

    @PostMapping("/doc")
    public ResponseEntity<ResponseDto> ingest(@RequestBody final DocumentDto document) {
        final int chunks = service.ingest(document.getText());
        return ResponseEntity.ok().body(new ResponseDto(chunks, 200));
    }

}
