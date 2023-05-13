package ru.practicum.shareit.request.validator;

import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;

public class ItemRequestValidator {

    public static void ItemRequestValid(ItemRequestCreationDto itemRequestDto) {
        if (itemRequestDto.getDescription() == null)
            throw new ValidationException("the request must have description");
        if (itemRequestDto.getDescription().isEmpty())
            throw new ValidationException("the description cannot be empty");
    }

    public static void PageValid(Integer from, Integer size) {
        if (from < 0 || size <= 0)
            throw new ValidationException("request body is missing");
    }

}
