package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.status.Role;
import ru.practicum.shareit.booking.status.State;
import ru.practicum.shareit.booking.validator.BookingValidator;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingClient client;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestBody @Valid BookingCreationDto bookingCreationDto,
            @RequestHeader("X-Sharer-User-Id") Integer userId) {
        BookingValidator.bookingValid(bookingCreationDto);
        log.info("--gateway-- POST booking {}, userId={}", bookingCreationDto, userId);
        return client.create(userId, bookingCreationDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findBookingDtoById(@PathVariable @Positive Integer bookingId,
                                                     @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("--gateway-- GET booking {}, userId={}", bookingId, userId);
        return client.findBookingDtoById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllBookingDtoByBooker(@RequestParam(name = "state", defaultValue = "ALL") String stateTitle,
                                                            @RequestParam(defaultValue = "0") Integer from,
                                                            @RequestParam(defaultValue = "20") Integer size,
                                                            @RequestHeader("X-Sharer-User-Id") Integer userId) {
        State state = State.getState(stateTitle);
        log.info("--gateway-- GET bookings with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return client.findAllBookingDtoByUser(state, from, size, userId, Role.BOOKER);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllBookingDtoByOwner(@RequestParam(name = "state", defaultValue = "ALL") String stateTitle,
                                                           @RequestParam(defaultValue = "0") Integer from,
                                                           @RequestParam(defaultValue = "20") Integer size,
                                                           @RequestHeader("X-Sharer-User-Id") Integer userId) {
        State state = State.getState(stateTitle);
        log.info("--gateway-- GET bookings with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return client.findAllBookingDtoByUser(state, from, size, userId, Role.OWNER);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@PathVariable @Positive Integer bookingId,
                                          @RequestParam Boolean approved,
                                          @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("--gateway-- PATCH booking approving, ownerId={}, bookingId={}, approved={}", userId, bookingId, approved);
        return client.approve(bookingId, approved, userId);
    }
}
