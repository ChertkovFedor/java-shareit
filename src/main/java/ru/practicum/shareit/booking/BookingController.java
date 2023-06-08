package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.status.Role;
import ru.practicum.shareit.booking.status.State;
import ru.practicum.shareit.booking.validator.BookingValidator;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bServ;

    @PostMapping
    public BookingDto create(
            @RequestBody @Valid BookingCreationDto bookingCreationDto,
            @RequestHeader("X-Sharer-User-Id") Integer userId) {
        BookingValidator.bookingValid(bookingCreationDto);
        return bServ.create(userId, bookingCreationDto);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findBookingDtoById(@PathVariable @Positive Integer bookingId,
                                         @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bServ.findBookingDtoById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> findAllBookingDtoByBooker(@RequestParam(name = "state", defaultValue = "ALL") String stateTitle,
                                                      @RequestParam(defaultValue = "0") Integer from,
                                                      @RequestParam(defaultValue = "20") Integer size,
                                                      @RequestHeader("X-Sharer-User-Id") Integer userId) {
        State state = State.getState(stateTitle);
        return bServ.findAllBookingDtoByUser(state, from, size, userId, Role.BOOKER);
    }

    @GetMapping("/owner")
    public List<BookingDto> findAllBookingDtoByOwner(@RequestParam(name = "state", defaultValue = "ALL") String stateTitle,
                                                     @RequestParam(defaultValue = "0") Integer from,
                                                     @RequestParam(defaultValue = "20") Integer size,
                                                     @RequestHeader("X-Sharer-User-Id") Integer userId) {
        State state = State.getState(stateTitle);
        return bServ.findAllBookingDtoByUser(state, from, size, userId, Role.OWNER);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@PathVariable @Positive Integer bookingId,
                              @RequestParam Boolean approved,
                              @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bServ.approve(bookingId, approved, userId);
    }
}
