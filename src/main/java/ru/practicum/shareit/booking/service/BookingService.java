package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Role;
import ru.practicum.shareit.booking.status.State;

import java.util.List;

public interface BookingService {
    BookingDto create(Integer userId, BookingCreationDto bookingCreationDto);

    BookingDto findBookingDtoById(Integer bookingId, Integer userId);

    Booking getBookingById(Integer bookingId);

    List<BookingDto> findAllBookingDtoByUser(State state, Integer from, Integer size, Integer userId, Role role);

    List<Booking> getAllBookingByItemIdIn(List<Integer> itemsId);

    BookingDto approve(Integer bookingId, Boolean approved, Integer userId);
}
