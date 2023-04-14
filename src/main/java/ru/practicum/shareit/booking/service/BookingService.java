package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    BookingDto create(Integer userId, BookingCreationDto bookingCreationDto);

    BookingDto findBookingDtoById(Integer bookingId, Integer userId);

    Booking getBookingById(Integer bookingId);

    List<BookingDto> findAllBookingDtoByBooker(String state, Integer userId);

    List<BookingDto> findAllBookingDtoByOwner(String state, Integer userId);

    List<Booking> getAllBookingByItemIdIn(List<Integer> itemsId);

    BookingDto approve(Integer bookingId, Boolean approved, Integer userId);
}
