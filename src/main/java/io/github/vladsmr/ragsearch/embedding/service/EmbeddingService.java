package io.github.vladsmr.ragsearch.embedding.service;

import lombok.AllArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import io.github.vladsmr.ragsearch.embedding.dto.EmbeddingDto;

@Service
@AllArgsConstructor
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;

    public EmbeddingDto generate(final EmbeddingDto embedding) {
        if (embedding == null || !StringUtils.hasText(embedding.getWord())) {
            return new EmbeddingDto();
        }
        final float[] em = embeddingModel.embed(embedding.getWord());
        final EmbeddingDto result = new EmbeddingDto();
        result.setEmbedding(em);
        result.setWord(embedding.getWord());
        return result;
    }

}
