package ru.practicum.shareit.booking.validator;

import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.UserMismatchException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

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
    }

    public static void itemBookingCreatValid(Item item, Integer userId) {
        if (!item.getAvailable())
            throw new ValidationException(String.format("Item with ID %s is not available for booking", item.getId()));
        if (item.getOwner().getId().equals(userId))
            throw new UserMismatchException("It is impossible to book your own item");
    }

    public static void itemBookingApproveValid(Booking booking, Boolean approved, Item item, Integer userId) {
        if (booking.getStatus() == Status.APPROVED && approved.equals(true))
            throw new ValidationException("The status is already approved");
        if (!item.getAvailable())
            throw new ValidationException(String.format("Item with ID %s is not available for booking", item.getId()));
        if (!item.getOwner().getId().equals(userId))
            throw new UserMismatchException("The item does not belong to this user");
    }

    public static void checkingViewer(Booking booking, Integer userId) {
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId))
            throw new UserMismatchException("This user does not have rights to view the booking");
    }

}
