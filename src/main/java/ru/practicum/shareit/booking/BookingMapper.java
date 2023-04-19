package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {

    public static Booking mapToModel(Integer id, BookingCreationDto bookingCreationDto, Item item, User user, Status status) {
        return new Booking(
                id,
                item,
                user,
                bookingCreationDto.getStart(),
                bookingCreationDto.getEnd(),
                status
        );
    }

    public static BookingDto mapToDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus()
        );
    }

    public static List<BookingDto> mapToListDto(List<Booking> bookings) {
        List<BookingDto> bookingsDto = new ArrayList<>();
        for (Booking b : bookings) {
            bookingsDto.add(mapToDto(b));
        }
        return bookingsDto;
    }

    public static BookingForItemDto mapToDtoForItem(Booking booking) {
        return new BookingForItemDto(
                booking.getId(),
                new BookingForItemDto.Item(booking.getItem().getId(), booking.getItem().getName()),
                booking.getBooker().getId(),
                booking.getStart(),
                booking.getEnd()
                );
    }

}
