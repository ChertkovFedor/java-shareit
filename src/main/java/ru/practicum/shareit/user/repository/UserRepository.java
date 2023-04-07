package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserCreationDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    Optional<UserDto> create(UserCreationDto userDto);

    Optional<UserDto> update(Integer userId, UserCreationDto userDto);

    Collection<UserDto> findAll();

    Optional<UserDto> findUserById(Integer userId);

    void delete(Integer userId);
}
