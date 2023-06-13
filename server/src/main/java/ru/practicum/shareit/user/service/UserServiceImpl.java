package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserCreationDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validator.UserValidator;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository uRep;

    @Override
    public UserDto create(UserCreationDto userDto) {
        UserValidator.userValid(userDto);
        User user = UserMapper.mapToModel(null, userDto);
        return UserMapper.mapToDto(uRep.save(user));
    }

    @Override
    public UserDto update(Integer userId, UserCreationDto userDto) {
        User user = getUserById(userId);
        if (userDto.getName() == null)
            userDto.setName(user.getName());
        if (userDto.getEmail() == null)
            userDto.setEmail(user.getEmail());
        UserValidator.userValid(userDto);
        user = UserMapper.mapToModel(userId, userDto);
        return UserMapper.mapToDto(uRep.save(user));
    }

    @Override
    public List<UserDto> findAll() {
        return UserMapper.mapToListDto(uRep.findAll());
    }

    @Override
    public UserDto findUserDtoById(Integer userId) {
        return UserMapper.mapToDto(getUserById(userId));
    }

    @Override
    public void deleteUserById(Integer userId) {
        uRep.delete(getUserById(userId));
    }

    @Override
    public User getUserById(Integer userId) {
        return uRep.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException(String.format("User with id %s not found", userId)));
    }

}
