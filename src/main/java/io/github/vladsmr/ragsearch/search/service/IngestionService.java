package io.github.vladsmr.ragsearch.search.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@AllArgsConstructor
public class IngestionService {

    /* TokenTextSplitter tuning notes.
     *
     * Known pitfalls:
     * 1. Trimmed tail is discarded: text between the punctuation trim point
     *    and the end of a window is lost, not carried into the next chunk.
     *    Verify by comparing total chunk length with the source length.
     * 2. No overlap support: sentences at window boundaries lose context.
     *    Punctuation trimming mitigates but does not solve this.
     * 3. CL100K tokenizer is OpenAI-oriented: ~1 token per 2-3 Cyrillic
     *    characters (vs ~4 for English), so token counts are approximate.
     *
     * Algorithm:
     * 1. Text → tokens (jtokkit, CL100K_BASE).
     * 2. Tokens <= chunkSize → single chunk.
     * 3. Otherwise → windows of chunkSize tokens.
     * 4. Each window is trimmed back to the last punctuation mark,
     *    no further than minChunkSizeChars from its start.
     * 5. Chunks shorter than minChunkLengthToEmbed tokens are discarded.
     * 6. Chunks beyond maxNumChunks are discarded.
     *
     * Evaluate chunking changes empirically with a golden dataset
     * (recall@k), not by feel. */
    private final TextSplitter textSplitter = TokenTextSplitter.builder()
                                                               .withChunkSize(150)                // chunk size in tokens
                                                               .withMinChunkSizeChars(100)        // punctuation trim boundary, chars
                                                               .withMinChunkLengthToEmbed(20)     // chunks below this are silently dropped
                                                               .withMaxNumChunks(100)             // hard cap per document
                                                               .withKeepSeparator(true)
                                                               .build();

    // minChunkLengthToEmbed: chunks shorter than the threshold are dropped before embedding.
    // A short text yields no chunks; the endpoint would still return 200 OK with nothing stored
    private final VectorStore vectorStore;

    public int ingest(final String text) {
        final Document document = new Document(text);
        final List<Document> chunks = textSplitter.apply(List.of(document));
        if (CollectionUtils.isEmpty(chunks)) {
            throw new IllegalArgumentException("text produced no chunks (too short or below minChunkLengthToEmbed)");
        }
        vectorStore.add(chunks);
        return chunks.size();
    }

}
