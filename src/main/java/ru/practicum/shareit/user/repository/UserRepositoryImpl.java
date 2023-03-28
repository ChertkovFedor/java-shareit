package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserCreationDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.validator.UserValidator;

import java.util.*;

@Component
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 1;

    @Override
    public Optional<UserDto> create(UserCreationDto userDto) {
        UserValidator.userCreationValid(userDto, users);
        User user = UserMapper.mapToModel(idGenerator, userDto);
        idGenerator++;
        users.put(user.getId(), user);
        log.info("User added: {}", user);
        return findUserById(user.getId());
    }

    @Override
    public Optional<UserDto> update(Integer userId, UserCreationDto userDto) {
        UserValidator.userUpdateValid(userId, userDto, users);
        User user = users.get(userId);
        if (userDto.getName() == null)
            userDto.setName(user.getName());
        if (userDto.getEmail() == null)
            userDto.setEmail(user.getEmail());
        user = UserMapper.mapToModel(userId, userDto);
        users.put(user.getId(), user);
        log.info("User updated: {}", user);
        return findUserById(user.getId());
    }

    @Override
    public Collection<UserDto> findAll() {
        return UserMapper.mapToUsersDto(users).values();
    }

    @Override
    public Optional<UserDto> findUserById(Integer userId) {
        if (!users.containsKey(userId)) throw new ObjectNotFoundException(
                String.format("User with id %s not found", userId));
        return Optional.of(UserMapper.mapToDto(users.get(userId)));
    }

    @Override
    public void delete(Integer userId) {
        users.remove(userId, users.get(userId));
        log.info("User deleted: {}", userId);
    }

}
