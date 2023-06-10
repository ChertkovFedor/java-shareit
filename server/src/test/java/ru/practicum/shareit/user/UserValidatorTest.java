package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserCreationDto;
import ru.practicum.shareit.user.validator.UserValidator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidatorTest {
    @Test
    public void testUserValid_ValidUser() {
        UserCreationDto userDto = new UserCreationDto("name", "name@mail.com");
        UserValidator.userValid(userDto);
    }

    @Test
    public void testUserValid_NullUser() {
        UserCreationDto userDto = null;
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            UserValidator.userValid(userDto);
        });
        assertThat(exception.getMessage()).isEqualTo("request body is missing");
    }

    @Test
    public void testUserValid_UserWithoutEmail() {
        UserCreationDto userDto = new UserCreationDto("name", null);
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            UserValidator.userValid(userDto);
        });
        assertThat(exception.getMessage()).isEqualTo("The user must have an email");
    }

    @Test
    public void testUserValid_UserWithInvalidEmail() {
        UserCreationDto userDto = new UserCreationDto("name", "name.mail.com");
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            UserValidator.userValid(userDto);
        });
        assertThat(exception.getMessage()).isEqualTo("the email must contain the character @");
    }

    @Test
    public void testUserValid_UserWithSpaceInEmail() {
        UserCreationDto userDto = new UserCreationDto("name", "na me@mail.com");
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            UserValidator.userValid(userDto);
        });
        assertThat(exception.getMessage()).isEqualTo("the email cannot contain spaces");
    }
}
