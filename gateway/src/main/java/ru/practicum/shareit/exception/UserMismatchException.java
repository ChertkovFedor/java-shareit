package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserMismatchException extends RuntimeException {
    public UserMismatchException(String m) {
        super(m);
        log.warn(m);
    }
}
