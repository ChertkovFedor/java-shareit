package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserCreationDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = UserController.class)
public class UserControllerTests {
    @MockBean
    private UserService uServ;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() throws Exception {
        UserCreationDto userDto = new UserCreationDto("name", "name@mail.com");
        UserDto createdUserDto = new UserDto(1, "name", "name@mail.com");

        when(uServ.create(any(UserCreationDto.class))).thenReturn(createdUserDto);

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"name\",\"email\":\"name@mail.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.email").value("name@mail.com"));

        verify(uServ, times(1)).create(any(UserCreationDto.class));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserCreationDto userDto = new UserCreationDto("name", "name@mail.com");
        UserDto updatedUserDto = new UserDto(1, "name", "name@mail.com");

        when(uServ.update(eq(1), any(UserCreationDto.class))).thenReturn(updatedUserDto);

        mvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"name\",\"email\":\"name@mail.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.email").value("name@mail.com"));

        verify(uServ, times(1)).update(eq(1), any(UserCreationDto.class));
    }

    @Test
    void testFindAllUsers() throws Exception {
        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(new UserDto(1, "name", "name@mail.com"));

        when(uServ.findAll()).thenReturn(userDtoList);

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].email").value("name@mail.com"));

        verify(uServ, times(1)).findAll();
    }

    @Test
    void testFindUserById() throws Exception {
        UserDto userDto = new UserDto(1, "name", "name@mail.com");

        when(uServ.findUserDtoById(1)).thenReturn(userDto);

        mvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.email").value("name@mail.com"));

        verify(uServ, times(1)).findUserDtoById(1);
    }

    @Test
    void testDeleteUserById() throws Exception {
        mvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(uServ, times(1)).deleteUserById(1);
    }
}



