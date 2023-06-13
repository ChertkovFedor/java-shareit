package ru.practicum.shareit.user.validator;

import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserCreationDto;

public class UserValidator {

    public static void userValid(UserCreationDto userDto) {
        if (userDto == null)
            throw new ValidationException("request body is missing");
        if (userDto.getEmail() == null)
            throw new ValidationException("The user must have an email");
        if (!userDto.getEmail().contains("@"))
            throw new ValidationException("the email must contain the character @");
        if (userDto.getEmail().contains(" "))
            throw new ValidationException("the email cannot contain spaces");
    }

}
