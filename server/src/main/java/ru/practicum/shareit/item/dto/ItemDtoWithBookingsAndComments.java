package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.comment.dto.CommentDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemDtoWithBookingsAndComments {
    private int id;
    @NotBlank(message = "the item must have name")
    private String name;
    @NotBlank(message = "the item must have description")
    @NotBlank(message = "the name cannot be empty")
    private String description;
    @NotNull(message = "item must have the status")
    private Boolean available;
    private BookingForItemDto lastBooking;
    private BookingForItemDto nextBooking;
    private List<CommentDto> comments;
}