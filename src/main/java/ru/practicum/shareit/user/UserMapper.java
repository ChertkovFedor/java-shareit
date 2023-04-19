package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreationDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static User mapToModel(Integer id, UserCreationDto userDto) {
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

    public static List<UserDto> mapToListDto(List<User> users) {
        List<UserDto> usersDto = new ArrayList<>();
        for (User u : users) {
            usersDto.add(mapToDto(u));
        }
        return usersDto;
    }

}
