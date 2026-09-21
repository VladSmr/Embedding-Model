package ru.embedding_model.ai_search.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OllamaService {

    private final OllamaChatModel chatModel;

    public String search(final List<Document> chunks, final String query) {
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

        // 3. Подставляем реальные данные в переменные шаблона
        final PromptTemplate promptTemplate = new PromptTemplate(template);
        final Prompt prompt = promptTemplate.create(Map.of("context", context, "question", query));

        // 4. Отправляем сформированный запрос в Ollama
        return chatModel.call(prompt).getResult().getOutput().getText();
    }

}
