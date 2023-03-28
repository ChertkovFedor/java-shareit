package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserCreationDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository rep;

    @Override
    public Optional<UserDto> create(UserCreationDto userDto) {
        return rep.create(userDto);
    }

    @Override
    public Optional<UserDto> update(Integer userId, UserCreationDto userDto) {
        return rep.update(userId, userDto);
    }

    @Override
    public Collection<UserDto> findAll() {
        return rep.findAll();
    }

    @Override
    public Optional<UserDto> findUserById(Integer userId) {
        return rep.findUserById(userId);
    }

    @Override
    public void deleteUserById(Integer userId) {
        rep.delete(userId);
    }
}
