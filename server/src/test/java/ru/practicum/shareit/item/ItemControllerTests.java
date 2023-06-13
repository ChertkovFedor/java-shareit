package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = ItemController.class)
class ItemControllerTests {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService iServ;

    @Autowired
    private MockMvc mvc;

    ItemCreationDto itemCreationDto1 = new ItemCreationDto(
            "item1",
            "description1",
            true,
            1
    );

    ItemDto itemDto1 = new ItemDto(
            1,
            "item1",
            "description1",
            true,
            1
    );

    ItemDto itemDto2 = new ItemDto(
            2,
            "item2",
            "description2",
            true,
            1
    );

    CommentCreateDto commentCreateDto = new CommentCreateDto("text");

    CommentDto commentDto = new CommentDto(
            1,
            "text",
            "author",
            Instant.now()
    );

    ItemDtoWithBookingsAndComments itemDtoFull1 = new ItemDtoWithBookingsAndComments(
            1,
            "item1",
            "description1",
            true,
            null,
            null,
            List.of(commentDto)
    );

    ItemDtoWithBookingsAndComments itemDtoFull2 = new ItemDtoWithBookingsAndComments(
            2,
            "item2",
            "description2",
            true,
            null,
            null,
            null
    );

    @Test
    void testCreate() throws Exception {

        when(iServ.create(itemCreationDto1, 1))
                .thenReturn(itemDto1);

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(itemCreationDto1))
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto1.getId()))
                .andExpect(jsonPath("$.name").value(itemDto1.getName()))
                .andExpect(jsonPath("$.description").value(itemDto1.getDescription()));

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(null))
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(itemCreationDto1))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdate() throws Exception {

        when(iServ.update(1, itemCreationDto1, 1))
                .thenReturn(itemDto1);
        mvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(itemDto1))
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto1.getId()))
                .andExpect(jsonPath("$.name").value(itemDto1.getName()))
                .andExpect(jsonPath("$.description").value(itemDto1.getDescription()));
    }

    @Test
    void testFindAll() throws Exception {

        when(iServ.findAll())
                .thenReturn(List.of(itemDto1, itemDto2));
        mvc.perform(get("/items/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0]").exists())
                .andExpect(jsonPath("$.[0].id").value(itemDto1.getId()))
                .andExpect(jsonPath("$.[0].name").value(itemDto1.getName()))
                .andExpect(jsonPath("$.[0].description").value(itemDto1.getDescription()));

    }

    @Test
    void testFindItemsByUser() throws Exception {

        when(iServ.findAllItemDtoByUser(any()))
                .thenReturn(List.of(itemDtoFull1, itemDtoFull2));
        mvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0]").exists())
                .andExpect(jsonPath("$.[0].id").value(itemDtoFull1.getId()))
                .andExpect(jsonPath("$.[0].name").value(itemDtoFull1.getName()))
                .andExpect(jsonPath("$.[0].description").value(itemDtoFull1.getDescription()));

    }

    @Test
    void testAddComment() throws Exception {

        when(iServ.addComment(any(), any(), any()))
                .thenReturn(commentDto);
        mvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(commentCreateDto))
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()))
                .andExpect(jsonPath("$.created").value(commentDto.getCreated().toString()));
    }

    @Test
    void testFindById() throws Exception {

        when(iServ.findItemDtoById(any(), any()))
                .thenReturn(itemDtoFull1);
        mvc.perform(get("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDtoFull1.getId()))
                .andExpect(jsonPath("$.name").value(itemDtoFull1.getName()))
                .andExpect(jsonPath("$.description").value(itemDtoFull1.getDescription()))
                .andExpect(jsonPath("$.comments").isArray())
                .andExpect(jsonPath("$.comments.[0].id").value(commentDto.getId()))
                .andExpect(jsonPath("$.comments.[0].text").value(commentDto.getText()))
                .andExpect(jsonPath("$.comments.[0].authorName").value(commentDto.getAuthorName()))
                .andExpect(jsonPath("$.comments.[0].created").value(commentDto.getCreated().toString()));
    }

    @Test
    void testSearch() throws Exception {

        when(iServ.searchAllItemDtoByNameOrDescription(any(), any()))
                .thenReturn(List.of(itemDto1, itemDto1));
        mvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("text", "abc")
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0]").exists())
                .andExpect(jsonPath("$.[0].id").value(itemDto1.getId()))
                .andExpect(jsonPath("$.[0].name").value(itemDto1.getName()))
                .andExpect(jsonPath("$.[0].description").value(itemDto1.getDescription()))
                .andExpect(jsonPath("$.[0].available").value(itemDto1.getAvailable()))
                .andExpect(jsonPath("$.[0].requestId").value(itemDto1.getRequestId()));

        when(iServ.searchAllItemDtoByNameOrDescription(any(), any()))
                .thenReturn(List.of());
        mvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("text", "")
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0]").doesNotExist());
    }

}