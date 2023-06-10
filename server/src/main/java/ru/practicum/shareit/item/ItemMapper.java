package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

    public static Item mapToModel(Integer id, ItemCreationDto itemDto, User owner) {
        return new Item(
                id,
                owner,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getRequestId()
        );
    }

    public static ItemDto mapToDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequestId()
        );
    }

    public static ItemDtoWithBookingsAndComments mapToDtoWithBookingsAndComments(
            Item item,
            BookingForItemDto lastBooking,
            BookingForItemDto nextBooking,
            List<CommentDto> comments) {
        return new ItemDtoWithBookingsAndComments(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                comments
        );
    }

    public static List<ItemDto> mapToListDto(List<Item> items) {
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item i : items) {
            itemsDto.add(mapToDto(i));
        }
        return itemsDto;
    }

}
