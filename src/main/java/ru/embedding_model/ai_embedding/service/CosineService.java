package ru.embedding_model.ai_embedding.service;

import lombok.AllArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.embedding_model.ai_embedding.dto.CosineDto;

@Service
@AllArgsConstructor
public class CosineService {

    private final EmbeddingModel embeddingModel;

    public CosineDto calculate(final CosineDto word) {
        final CosineDto cosine = validateInputDate(word);

        if (StringUtils.hasText(cosine.getError())) {
            return cosine;
        }

        final float[] vectorOne = embeddingModel.embed(cosine.getWordOne());
        final float[] vectorTwo = embeddingModel.embed(cosine.getWordTwo());

        float dotProduct = 0.0F;
        float normA = 0.0F;
        float normB = 0.0F;

        for (int i = 0; i < vectorOne.length; i++) {
            float a = vectorOne[i];
            float b = vectorTwo[i];

            dotProduct += a * b;
            normA += a * a;
            normB += b * b;
        }

        if (normA == 0.0) {
            cosine.setProximity(0.0F);
            cosine.setError("word one is invalid");
            return cosine;
        }
        if (normB == 0.0) {
            cosine.setProximity(0.0F);
            cosine.setError("word two is invalid");
            return cosine;
        }

        final float rawCosine = dotProduct / (float) (Math.sqrt(normA) * Math.sqrt(normB));
        final float proximity = Math.max(0.0F, Math.min(1.0F, rawCosine));

        cosine.setProximity(proximity);
        return cosine;

    }

    private CosineDto validateInputDate(final CosineDto cosine) {
        if (cosine == null) {
            final CosineDto result = new CosineDto();
            result.setError("invalid args");
            return result;
        }
        if (!StringUtils.hasText(cosine.getWordOne()) || !StringUtils.hasText(cosine.getWordTwo())) {
            cosine.setError("invalid args");
            return cosine;
        }
        final String wordOneRequest = cosine.getWordOne();
        final String wordTwoRequest = cosine.getWordTwo();
        final String[] wordOne = wordOneRequest.trim().split(" ");
        final String[] wordTwo = wordTwoRequest.trim().split(" ");

        if (wordOne.length > 1 || wordTwo.length > 1) {
            cosine.setError("invalid args");
            return cosine;
        }
        final CosineDto result = new CosineDto();
        result.setWordOne(wordOneRequest.trim());
        result.setWordTwo(wordTwoRequest.trim());
        return result;
    }

}
