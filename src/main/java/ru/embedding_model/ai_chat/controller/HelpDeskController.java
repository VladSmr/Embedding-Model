package ru.embedding_model.ai_chat.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.embedding_model.ai_chat.dto.HelpDeskRequest;
import ru.embedding_model.ai_chat.dto.HelpDeskResponse;
import ru.embedding_model.ai_chat.service.HelpDeskChatbotAgentService;

@RestController
@AllArgsConstructor
@RequestMapping("/chat")
public class HelpDeskController {

    private final HelpDeskChatbotAgentService service;

    @PostMapping("/ask")
    public ResponseEntity<HelpDeskResponse> ask(@RequestBody HelpDeskRequest request) {
        var response = service.call(request.getPromptMessage(), request.getHistoryId());
        return new ResponseEntity<>(new HelpDeskResponse(response), HttpStatus.OK);
    }

}
