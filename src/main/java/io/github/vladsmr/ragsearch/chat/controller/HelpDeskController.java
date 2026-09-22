package io.github.vladsmr.ragsearch.chat.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.vladsmr.ragsearch.chat.dto.HelpDeskRequest;
import io.github.vladsmr.ragsearch.chat.dto.HelpDeskResponse;
import io.github.vladsmr.ragsearch.chat.service.HelpDeskChatService;

@RestController
@AllArgsConstructor
@RequestMapping("/chat")
public class HelpDeskController {

    private final HelpDeskChatService service;

    @PostMapping("/ask")
    public ResponseEntity<HelpDeskResponse> ask(@RequestBody HelpDeskRequest request) {
        var response = service.call(request.getPromptMessage(), request.getHistoryId());
        return ResponseEntity.ok().body(new HelpDeskResponse(response));
    }

}
