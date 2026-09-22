package io.github.vladsmr.ragsearch.search.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.vladsmr.ragsearch.search.dto.DocumentDto;
import io.github.vladsmr.ragsearch.search.dto.IngestionResponse;
import io.github.vladsmr.ragsearch.search.service.IngestionService;

@RestController
@RequestMapping("/ingest")
@AllArgsConstructor
public class IngestionController {

    private final IngestionService service;

    @PostMapping("/doc")
    public ResponseEntity<IngestionResponse> ingest(@RequestBody final DocumentDto document) {
        final int chunks = service.ingest(document.getText());
        return ResponseEntity.ok().body(new IngestionResponse(chunks, 200));
    }

}
