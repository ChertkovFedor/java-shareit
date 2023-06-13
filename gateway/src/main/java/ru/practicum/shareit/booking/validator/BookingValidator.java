package ru.practicum.shareit.booking.validator;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;

@Slf4j
public class BookingValidator {

    public static void bookingValid(BookingCreationDto bookingDto) {
        if (bookingDto.getStart() == null)
            throw new ValidationException("The booking must have a start time");
        if (bookingDto.getEnd() == null)
            throw new ValidationException("The booking must have a end time");
        if (bookingDto.getStart().isAfter(bookingDto.getEnd()))
            throw new ValidationException("Booking cannot start later than the end");
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()))
            throw new ValidationException("Booking cannot end earlier than the beginning");
        if (bookingDto.getEnd().equals(bookingDto.getStart()))
            throw new ValidationException("The start and end time of the booking cannot be equal");
        if (bookingDto.getStart().isBefore(LocalDateTime.now()))
            throw new ValidationException("Booking cannot start in the past");
        if (bookingDto.getEnd().isBefore(LocalDateTime.now()))
            throw new ValidationException("Booking cannot and in the past");
        log.info("bookingValid ----------- OK");
    }

}
