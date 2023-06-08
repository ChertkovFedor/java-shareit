package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.status.Role;
import ru.practicum.shareit.booking.status.State;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = BookingController.class)
class BookingControllerTests {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService bServ;

    @Autowired
    private MockMvc mvc;

    private final User user = new User(
            1,
            "user1",
            "user1@mail.ru");

    Item item = new Item(
            1,
            null,
            "item1",
            "description1",
            true,
            1
    );

    BookingDto bookingDto = new BookingDto(
            1,
            item,
            user,
            LocalDateTime.parse("2023-05-22T19:12:08"),
            LocalDateTime.parse("2023-05-22T19:12:10"),
            Status.WAITING
    );

    @Test
    void testCreate() throws Exception {

        BookingCreationDto bookingCreationDto = new BookingCreationDto(
                1,
                LocalDateTime.now().plusSeconds(10),
                LocalDateTime.now().plusSeconds(100)
        );

        when(bServ.create(1, bookingCreationDto))
                .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(bookingCreationDto))
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()));

        bookingCreationDto = new BookingCreationDto(
                1,
                LocalDateTime.now().minusSeconds(10),
                LocalDateTime.now().plusSeconds(100)
        );

        when(bServ.create(1, bookingCreationDto))
                .thenReturn(bookingDto);

        BookingCreationDto bookingCreationDto2 = bookingCreationDto;

        Assertions.assertThatThrownBy(() ->
                        mvc.perform(post("/bookings")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding(StandardCharsets.UTF_8)
                                        .content(mapper.writeValueAsString(bookingCreationDto2))
                                        .header("X-Sharer-User-Id", "1")
                                        .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest()))
                .hasCauseInstanceOf(RuntimeException.class).hasMessageContaining("Booking cannot start in the past");
    }

    @Test
    void testCreateVerifyCallingTimes() throws Exception {
        BookingCreationDto bookingCreationDto = new BookingCreationDto(
                1,
                LocalDateTime.now().plusSeconds(10),
                LocalDateTime.now().plusSeconds(100)
        );

        when(bServ.create(1, bookingCreationDto))
                .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(bookingCreationDto))
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        Mockito.verify(bServ, Mockito.times(1))
                .create(1, bookingCreationDto);
    }

    @Test
    void testFindBookingDtoById() throws Exception {

        when(bServ.findBookingDtoById(1, 1))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()));

        mvc.perform(get("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFindAll() throws Exception {

        when(bServ.findAllBookingDtoByUser(State.CURRENT, 0, 100, 1, Role.BOOKER))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("state", "CURRENT")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(100))
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0]").exists());

        when(bServ.findAllBookingDtoByUser(State.CURRENT, 0, 100, 1, Role.BOOKER))
                .thenReturn(List.of(bookingDto));

        Assertions.assertThatThrownBy(() ->
                        mvc.perform(get("/bookings")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .characterEncoding(StandardCharsets.UTF_8)
                                        .param("state", "WRONG_STATUS")
                                        .param("from", String.valueOf(0))
                                        .param("size", String.valueOf(100))
                                        .header("X-Sharer-User-Id", "1")
                                        .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest()))
                .hasCauseInstanceOf(RuntimeException.class).hasMessageContaining("Unknown state: WRONG_STATUS");
    }

    @Test
    void testApprove() throws Exception {

        when(bServ.approve(
                1, true, 1))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", "1")
                        .param("approved", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()));

        mvc.perform(patch("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSearch() throws Exception {

        when(bServ.findAllBookingDtoByUser(State.CURRENT, 0, 100, 1, Role.OWNER))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(100))
                        .param("state", "CURRENT")
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0]").exists());

        when(bServ.findAllBookingDtoByUser(State.CURRENT, 0, 100, 1, Role.OWNER))
                .thenReturn(List.of(bookingDto));

        Assertions.assertThatThrownBy(() ->
                        mvc.perform(get("/bookings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .param("from", String.valueOf(0))
                                .param("size", String.valueOf(100))
                                .param("state", "WRONG_STATUS")
                                .header("X-Sharer-User-Id", "1")
                                .accept(MediaType.APPLICATION_JSON)))
                .hasCauseInstanceOf(RuntimeException.class).hasMessageContaining("Unknown state: WRONG_STATUS");
    }

}