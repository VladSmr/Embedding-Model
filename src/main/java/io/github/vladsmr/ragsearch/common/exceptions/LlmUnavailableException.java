package io.github.vladsmr.ragsearch.common.exceptions;

public class LlmUnavailableException extends RuntimeException {

    public LlmUnavailableException(String message) {
        super(message);
    }

}
