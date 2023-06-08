package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = ItemRequestController.class)
public class ItemRequestControllerTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private ItemRequestService irServ;

    @Mock
    UserService uServ;

    ItemRequestDto itemRequestDto = new ItemRequestDto(1, "description", Instant.now(), List.of());
    ItemRequestCreationDto itemRequestCreationDto = new ItemRequestCreationDto("description");
    private final User user = new User(
            1,
            "user1",
            "user1@mail.ru");

    @Test
    void testGetAllItemRequests() throws Exception {
        List<ItemRequestDto> itemRequests = new ArrayList<>();

        when(irServ.findAllRequestDtoByUser(anyInt()))
                .thenReturn(itemRequests);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(itemRequests.size()));
    }

    @Test
    void testGetItemRequestById() throws Exception {

        when(irServ.getRequestDtoById(anyInt(), anyInt()))
                .thenReturn(itemRequestDto);

        mvc.perform(get("/requests/{requestId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()));
    }

    @Test
    void testCreateItemRequest() throws Exception {

        when(irServ.create(itemRequestCreationDto, 1))
                .thenReturn(itemRequestDto);

        when(uServ.getUserById(any()))
                .thenReturn(user);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(itemRequestCreationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()));
    }

}
