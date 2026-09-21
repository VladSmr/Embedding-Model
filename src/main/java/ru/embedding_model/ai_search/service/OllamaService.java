package ru.embedding_model.ai_search.service;

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

@Service
@AllArgsConstructor
public class OllamaService {

    private final OllamaChatModel chatModel;

    private String callOllama(final Prompt prompt) {
        ChatResponse response;
        try {
            response = chatModel.call(prompt);
        } catch (final RuntimeException e) {
            return String.format("error calling ollama: %s", e.getMessage());
        }
        if (response.getResult() == null) {
            return "Chat response is empty. Something went wrong";
        }
        return response.getResult().getOutput().getText();
    }

    private String emptyChunksResponse(final String query) {
        final String template = """
                Ты полезный ассистент. Ответь на вопрос пользователя. Поиск по контексту не дал результата. Сообщи об это пользователю.
                Затем предоставь ответ, который ты сам считаешь верным.
                
                ВОПРОС:
                {question}
                
                ОТВЕТ:
                """;

        // Подставляем реальные данные в переменные шаблона
        final PromptTemplate promptTemplate = new PromptTemplate(template);
        final Prompt prompt = promptTemplate.create(Map.of("question", query));

        // Отправляем сформированный запрос в Ollama
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
                Ты полезный ассистент. Ответь на вопрос пользователя, используя предоставленный ниже контекст.
                Если в контексте нет ответа, так и скажи: "Я не знаю ответа на этот вопрос на основе предоставленных документов".
                Затем предоставь ответ, который ты сам считаешь верным.
                
                КОНТЕКСТ:
                {context}
                
                ВОПРОС:
                {question}
                
                ОТВЕТ:
                """;

        // Подставляем реальные данные в переменные шаблона
        final PromptTemplate promptTemplate = new PromptTemplate(template);
        final Prompt prompt = promptTemplate.create(Map.of("context", context, "question", query));

        // Отправляем сформированный запрос в Ollama
        return callOllama(prompt);
    }

}
