package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreationDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserMapper {

    public static User mapToModel(int id, UserCreationDto userDto) {
        return new User(
                id,
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public static UserDto mapToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static Map<Integer, UserDto> mapToUsersDto(Map<Integer, User> users) {
        Map<Integer, UserDto> usersDto = new HashMap<>();
        for (User u : users.values()) {
            UserDto userDto = UserMapper.mapToDto(u);
            usersDto.put(userDto.getId(), userDto);
        }
        return usersDto;
    }

}
