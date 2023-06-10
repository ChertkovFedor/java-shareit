package ru.practicum.shareit.booking.status;

import ru.practicum.shareit.exception.StateException;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static State getState(String state) {
        try {
            return State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new StateException(String.format("Unknown state: %s", state));
        }
    }
}