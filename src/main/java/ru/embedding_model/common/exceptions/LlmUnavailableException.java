package ru.embedding_model.common.exceptions;

public class LlmUnavailableException extends RuntimeException {

    public LlmUnavailableException(String message) {
        super(message);
    }

}
