package io.github.vladsmr.ragsearch.search.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import io.github.vladsmr.ragsearch.common.exceptions.LlmUnavailableException;

@Service
@AllArgsConstructor
public class LlmService {

    private final OllamaChatModel chatModel;

    private String callOllama(final Prompt prompt) {
        ChatResponse response;
        try {
            response = chatModel.call(prompt);
        } catch (final RuntimeException e) {
            throw new LlmUnavailableException(String.format("error calling ollama: %s", e.getMessage()));
        }
        if (response.getResult() == null) {
            return "Chat response is empty. Something went wrong";
        }
        return response.getResult().getOutput().getText();
    }

    private String emptyChunksResponse(final String query) {
        final String template = """
                You are a helpful assistant. Answer the user's question. The context search returned no results. Inform the user about this.
                Then provide the answer you believe is correct.
                
                QUESTION:
                {question}
                
                ANSWER:
                """;

        // Bind actual values to the template variables
        final PromptTemplate promptTemplate = new PromptTemplate(template);
        final Prompt prompt = promptTemplate.create(Map.of("question", query));

        // Send the composed prompt to Ollama
        return callOllama(prompt);
    }

    public String search(final List<Document> chunks, final String query) {
        if (CollectionUtils.isEmpty(chunks)) {
            return emptyChunksResponse(query);
        }
        final String context = chunks.stream()
                                     .map(Document::getText)
                                     .collect(Collectors.joining("\n\n"));

        final String template = """
                You are a helpful assistant. Answer the user's question using the context provided below.
                If the context does not contain the answer, say so: "I don't know the answer based on the provided documents".
                Then provide the answer you believe is correct.
                
                CONTEXT:
                {context}
                
                QUESTION:
                {question}
                
                ANSWER:
                """;

        // Bind actual values to the template variables
        final PromptTemplate promptTemplate = new PromptTemplate(template);
        final Prompt prompt = promptTemplate.create(Map.of("context", context, "question", query));

        // Send the composed prompt to Ollama
        return callOllama(prompt);
    }

}
