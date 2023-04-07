package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationException extends RuntimeException {
    public ValidationException(final String m) {
        super(m);
        log.info("Validation failed: " + m);
    }

}
