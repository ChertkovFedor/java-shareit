package ru.practicum.shareit.item.validator;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

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

    public static void checkingOwner(Item item, Integer ownerId) {
        if (!item.getOwner().getId().equals(ownerId))
            throw new ObjectNotFoundException(String.format("Owner with id %s not found", ownerId));
    }

    public static void checkingPastBooking(List<Booking> pastBookings, Integer authorId) {
        if (pastBookings.isEmpty())
            throw new ValidationException("The item was not booked");
        if (pastBookings.stream()
                .map(b -> b.getBooker().getId())
                .noneMatch(id -> id.equals(authorId)))
            throw new ValidationException(String.format("The item was not booked by the user with the id %s", authorId));
    }

    public static void commentValid(CommentCreateDto commentCreateDto) {
        if (commentCreateDto.getText().isEmpty())
            throw new ValidationException("The comment cannot be empty");
    }

}
