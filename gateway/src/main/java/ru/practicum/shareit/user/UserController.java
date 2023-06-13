package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreationDto;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserCreationDto userDto) {
        log.info("--gateway-- POST user {}", userDto);
        return client.create(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable(value = "userId") Integer userId,
                          @RequestBody @Valid UserCreationDto userDto) {
        log.info("--gateway-- PATCH user {}, userId={}", userDto, userId);
        return client.update(userId, userDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return client.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object>  getUserById(@PathVariable(value = "userId") Integer userId) {
        log.info("--gateway-- GET user, userId={}", userId);
        return client.findUserDtoById(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable(value = "userId") Integer userId) {
        log.info("--gateway-- DELETE user, userId={}", userId);
        client.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
