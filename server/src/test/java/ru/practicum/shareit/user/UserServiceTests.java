package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserCreationDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTests {
    @Mock
    private UserRepository uRep;

    @InjectMocks
    private UserServiceImpl uServ;

    public UserServiceTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        UserCreationDto userDto = new UserCreationDto("name", "name@mail.com");
        User createdUser = new User(1, "name", "name@mail.com");
        UserDto expectedUserDto = new UserDto(1, "name", "name@mail.com");

        when(uRep.save(any(User.class))).thenReturn(createdUser);

        UserDto result = uServ.create(userDto);

        assertEquals(expectedUserDto, result);
        verify(uRep, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser() {
        Integer userId = 1;
        UserCreationDto userDto = new UserCreationDto("name", "name@mail.com");
        User existingUser = new User(1, "name", "name2@mail.com");
        User updatedUser = new User(1, "name", "name@mail.com");
        UserDto expectedUserDto = new UserDto(1, "name", "name@mail.com");

        when(uRep.findById(userId)).thenReturn(Optional.of(existingUser));
        when(uRep.save(any(User.class))).thenReturn(updatedUser);

        UserDto result = uServ.update(userId, userDto);

        assertEquals(expectedUserDto, result);
        verify(uRep, times(1)).findById(userId);
        verify(uRep, times(1)).save(any(User.class));
    }

    @Test
    void testFindAllUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "name", "name@mail.com"));

        when(uRep.findAll()).thenReturn(userList);

        List<UserDto> result = uServ.findAll();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("name", result.get(0).getName());
        assertEquals("name@mail.com", result.get(0).getEmail());
        verify(uRep, times(1)).findAll();
    }

    @Test
    void testFindUserById() {
        Integer userId = 1;
        User user = new User(1, "name", "name@mail.com");
        UserDto expectedUserDto = new UserDto(1, "name", "name@mail.com");

        when(uRep.findById(userId)).thenReturn(Optional.of(user));

        UserDto result = uServ.findUserDtoById(userId);

        assertEquals(expectedUserDto, result);
        verify(uRep, times(1)).findById(userId);
    }

    @Test
    void testDeleteUserById() {
        Integer userId = 1;

        when(uRep.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> uServ.deleteUserById(userId));
        verify(uRep, times(1)).findById(userId);
        verify(uRep, never()).delete(any(User.class));
    }

    @Test
    void testGetUserById() {
        Integer userId = 1;
        User user = new User(1, "name", "name@mail.com");

        when(uRep.findById(userId)).thenReturn(Optional.of(user));

        User result = uServ.getUserById(userId);

        assertEquals(user, result);
        verify(uRep, times(1)).findById(userId);
    }
}

