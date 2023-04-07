package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreationDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public Optional<UserDto> create(@RequestBody UserCreationDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping("/{userId}")
    public Optional<UserDto> update(@PathVariable(value = "userId") Integer userId, @RequestBody UserCreationDto userDto) {
        return userService.update(userId, userDto);
    }

    @GetMapping
    public Collection<UserDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public Optional<UserDto> getUserById(@PathVariable(value = "userId") Integer userId) {
        return userService.findUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable(value = "userId") Integer userId) {
        userService.deleteUserById(userId);
    }

}
