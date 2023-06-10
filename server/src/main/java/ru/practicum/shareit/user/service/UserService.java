package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserCreationDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    UserDto create(UserCreationDto userDto);

    UserDto update(Integer userId, UserCreationDto userDto);

    List<UserDto> findAll();

    UserDto findUserDtoById(Integer userId);

    void deleteUserById(Integer userId);

    User getUserById(Integer userId);
}
