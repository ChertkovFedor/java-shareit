package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.validator.ItemRequestValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ItemRequestValidatorTest {

    @Mock
    private ItemRequestCreationDto itemRequestDto;

    @Test
    public void testItemRequestValid_WithNullDescription_ThrowsValidationException() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(itemRequestDto.getDescription()).thenReturn(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            ItemRequestValidator.itemRequestValid(itemRequestDto);
        });

        assertEquals("the request must have description", exception.getMessage());
    }

    @Test
    public void testItemRequestValid_WithEmptyDescription_ThrowsValidationException() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(itemRequestDto.getDescription()).thenReturn("");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            ItemRequestValidator.itemRequestValid(itemRequestDto);
        });

        assertEquals("the description cannot be empty", exception.getMessage());
    }

    @Test
    public void testItemRequestValid_WithValidDescription_NoExceptionThrown() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(itemRequestDto.getDescription()).thenReturn("Valid description");

        ItemRequestValidator.itemRequestValid(itemRequestDto);
    }

    @Test
    public void testPageValid_WithNegativeFromValue_ThrowsValidationException() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            ItemRequestValidator.pageValid(-1, 10);
        });

        assertEquals("request body is missing", exception.getMessage());
    }

    @Test
    public void testPageValid_WithZeroSize_ThrowsValidationException() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            ItemRequestValidator.pageValid(0, 0);
        });

        assertEquals("request body is missing", exception.getMessage());
    }

    @Test
    public void testPageValid_WithValidFromAndSize_NoExceptionThrown() {
        ItemRequestValidator.pageValid(0, 10);
    }
}
