package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.booking.validator.BookingValidator;
import ru.practicum.shareit.exception.UserMismatchException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class BookingValidatorTest {
    @Mock
    private Item item;
    @Mock
    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBookingValid() {
        BookingCreationDto bookingDto = new BookingCreationDto(1, null, LocalDateTime.now());
        assertThrows(ValidationException.class, () -> {
            try {
                BookingValidator.bookingValid(bookingDto);
            } catch (ValidationException e) {
                assertEquals("The booking must have a start time", e.getMessage());
                throw e;
            }
        });

        BookingCreationDto bookingDto2 = new BookingCreationDto(1, LocalDateTime.now().minusSeconds(10), LocalDateTime.now());
        assertThrows(ValidationException.class, () -> {
            try {
                BookingValidator.bookingValid(bookingDto2);
            } catch (ValidationException e) {
                assertEquals("Booking cannot start in the past", e.getMessage());
                throw e;
            }
        });
    }

    @Test
    public void testItemBookingCreatValid() {
        when(item.getAvailable()).thenReturn(false);
        when(item.getId()).thenReturn(1);
        when(item.getOwner()).thenReturn(user);
        when(user.getId()).thenReturn(1);

        assertThrows(ValidationException.class, () -> {
            try {
                BookingValidator.itemBookingCreatValid(item, 1);
            } catch (ValidationException e) {
                assertEquals("Item with ID 1 is not available for booking", e.getMessage());
                throw e;
            }
        });
    }

    @Test
    public void testItemBookingApproveValid() {
        Booking booking = new Booking();
        booking.setStatus(Status.APPROVED);

        when(item.getAvailable()).thenReturn(false);
        when(item.getId()).thenReturn(1);
        when(item.getOwner()).thenReturn(user);
        when(user.getId()).thenReturn(1);

        assertThrows(ValidationException.class, () -> {
            try {
                BookingValidator.itemBookingApproveValid(booking, true, item, 1);
            } catch (ValidationException e) {
                assertEquals("The status is already approved", e.getMessage());
                throw e;
            }
        });
    }

    @Test
    public void testCheckingViewer() {
        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);

        when(user.getId()).thenReturn(1);
        when(item.getOwner()).thenReturn(user);

        assertThrows(UserMismatchException.class, () -> {
            try {
                BookingValidator.checkingViewer(booking, 2);
            } catch (UserMismatchException e) {
                assertEquals("This user does not have rights to view the booking", e.getMessage());
                throw e;
            }
        });
    }

    @Test
    public void testPageValid() {
        assertThrows(ValidationException.class, () -> {
            try {
                BookingValidator.pageValid(-1, 10);
            } catch (ValidationException e) {
                assertEquals("request body is missing", e.getMessage());
                throw e;
            }
        });
    }
}
