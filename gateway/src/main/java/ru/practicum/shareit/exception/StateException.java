package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StateException extends RuntimeException {
    public StateException(final String m) {
        super(m);
        log.warn(m);
    }

}
