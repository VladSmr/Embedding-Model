package io.github.vladsmr.ragsearch.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import io.github.vladsmr.ragsearch.common.exceptions.LlmUnavailableException;
import io.github.vladsmr.ragsearch.common.exceptions.OpenSearchUnavailableException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LlmUnavailableException.class)
    public ResponseEntity<ProblemDetail> handleLlm(final LlmUnavailableException exception) {
        final ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(pd);
    }

    @ExceptionHandler(OpenSearchUnavailableException.class)
    public ResponseEntity<ProblemDetail> handleOs(final OpenSearchUnavailableException exception) {
        final ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, exception.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(pd);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleValidation(final IllegalArgumentException exception) {
        final ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pd);
    }

}
