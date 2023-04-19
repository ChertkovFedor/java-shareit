package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreationDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService uServ;

    @PostMapping
    public UserDto create(@RequestBody @Valid UserCreationDto userDto) {
        return uServ.create(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable(value = "userId") Integer userId,
                          @RequestBody @Valid UserCreationDto userDto) {
        return uServ.update(userId, userDto);
    }

    @GetMapping
    public List<UserDto> findAll() {
        return uServ.findAll();
    }

    @GetMapping("/{userId}")
    public UserDto  getUserById(@PathVariable(value = "userId") Integer userId) {
        return uServ.findUserDtoById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable(value = "userId") Integer userId) {
        uServ.deleteUserById(userId);
    }

}
