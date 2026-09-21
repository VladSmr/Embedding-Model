package ru.embedding_model.ai_search.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.embedding_model.ai_search.service.SearchService;

@RestController
@RequestMapping("/search")
@AllArgsConstructor
public class SearchController {

    private final SearchService service;

    @GetMapping("/llm-query")
    public ResponseEntity<String> llmSearch(@RequestParam final String q) {
        final String result = service.llmSearch(q);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/query")
    public ResponseEntity<List<Map<String, String>>> search(@RequestParam final String q) {
        var docs = service.search(q);
        var result = docs.stream()
                         .filter(Objects::nonNull)
                         .map(doc -> Map.of("text", doc.getText(), "score", String.valueOf(doc.getScore())))
                         .toList();

        return ResponseEntity.ok(result);
    }

}
