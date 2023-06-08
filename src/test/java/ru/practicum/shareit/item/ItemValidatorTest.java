package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.validator.ItemValidator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.booking.status.Status;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ItemValidatorTest {

    @Test
    public void testItemValid_NullItemDto_ThrowsValidationException() {
        assertThrows(ValidationException.class, () -> ItemValidator.itemValid(null), "request body is missing");
    }

    @Test
    public void testItemValid_NullName_ThrowsValidationException() {
        ItemCreationDto itemDto = new ItemCreationDto(null, "description", true, 123);
        assertThrows(ValidationException.class, () -> ItemValidator.itemValid(itemDto), "the item must have name");
    }

    @Test
    public void testItemValid_EmptyName_ThrowsValidationException() {
        ItemCreationDto itemDto = new ItemCreationDto("", "description", true, 123);
        assertThrows(ValidationException.class, () -> ItemValidator.itemValid(itemDto), "the name cannot be empty");
    }

    @Test
    public void testItemValid_NullDescription_ThrowsValidationException() {
        ItemCreationDto itemDto = new ItemCreationDto("name", null, true, 123);
        assertThrows(ValidationException.class, () -> ItemValidator.itemValid(itemDto), "the item must have description");
    }

    @Test
    public void testItemValid_NullAvailable_ThrowsValidationException() {
        ItemCreationDto itemDto = new ItemCreationDto("name", "description", null, 123);
        assertThrows(ValidationException.class, () -> ItemValidator.itemValid(itemDto), "item must have the status");
    }

    @Test
    public void testCheckingOwner_OwnerIdMismatch_ThrowsObjectNotFoundException() {
        Item item = mock(Item.class);
        User owner = new User(1, "name", "name@mail.com");
        when(item.getOwner()).thenReturn(owner);

        assertThrows(ObjectNotFoundException.class, () -> ItemValidator.checkingOwner(item, 2),
                "Owner with id 2 not found");
    }

    @Test
    public void testCheckingPastBooking_EmptyPastBookings_ThrowsValidationException() {
        assertThrows(ValidationException.class, () -> ItemValidator.checkingPastBooking(new ArrayList<>(), 1),
                "The item was not booked");
    }

    @Test
    public void testCheckingPastBooking_BookerIdMismatch_ThrowsValidationException() {
        Booking booking1 = new Booking(1, new Item(), new User(2, "name2", "name2@mail.com"), null, null, Status.WAITING);
        Booking booking2 = new Booking(2, new Item(), new User(3, "name3", "name3@mail.com"), null, null, Status.WAITING);
        List<Booking> pastBookings = List.of(booking1, booking2);

        assertThrows(ValidationException.class, () -> ItemValidator.checkingPastBooking(pastBookings, 4),
                "The item was not booked by the user with the id 4");
    }

    @Test
    public void testCommentValid_EmptyText_ThrowsValidationException() {
        CommentCreateDto commentCreateDto = new CommentCreateDto("");
        assertThrows(ValidationException.class, () -> ItemValidator.commentValid(commentCreateDto),
                "The comment cannot be empty");
    }
}
