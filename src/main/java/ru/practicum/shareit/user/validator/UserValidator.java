package ru.practicum.shareit.user.validator;

import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserCreationDto;

import java.util.*;

public class UserValidator {

    public static void userCreationValid(UserCreationDto userDto, Map<Integer, User> users) {
        userNotBody(userDto);
        userEmailValid(userDto);
        if (!users.isEmpty())
            if (getEmails(users).containsKey(userDto.getEmail()))
                throw new AlreadyExistException("user with this email already exists");
    }

    public static void userUpdateValid(Integer userId, UserCreationDto userDto, Map<Integer, User> users) {
        userNotBody(userDto);
        if (userDto.getEmail() != null)
            userEmailValid(userDto);
        if (!users.isEmpty())
            if (getEmails(users).containsKey(userDto.getEmail()))
                if (!userId.equals(getEmails(users).get(userDto.getEmail())))
                    throw new AlreadyExistException("user with this email already exists");
    }

    private static void userNotBody(UserCreationDto userDto) {
        if (userDto == null)
            throw new ValidationException("request body is missing");
    }

    private static void userEmailValid(UserCreationDto userDto) {
        if (userDto.getEmail() == null)
            throw new ValidationException("the user must have email");
        if (userDto.getEmail().isEmpty())
            throw new ValidationException("the email cannot be empty");
        if (!userDto.getEmail().contains("@"))
            throw new ValidationException("the email must contain the character @");
        if (userDto.getEmail().contains(" "))
            throw new ValidationException("the email cannot contain spaces");
    }

    private static Map<String, Integer> getEmails(Map<Integer, User> users) {
        Map<String, Integer> emails = new HashMap<>();
        for (User user : users.values())
            emails.put(user.getEmail(), user.getId());
        return emails;
    }

}
