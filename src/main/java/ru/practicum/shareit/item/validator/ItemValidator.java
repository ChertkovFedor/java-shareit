package ru.practicum.shareit.item.validator;

import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Map;

public class ItemValidator {
    public static void itemValid(ItemCreationDto itemDto) {
        if (itemDto == null)
            throw new ValidationException("request body is missing");
        if (itemDto.getName() == null)
            throw new ValidationException("the item must have name");
        if (itemDto.getName().isEmpty())
            throw new ValidationException("the name cannot be empty");
        if (itemDto.getDescription() == null)
            throw new ValidationException("the item must have description");
        if (itemDto.getAvailable() == null)
            throw new ValidationException("item must have the status");
    }

}
