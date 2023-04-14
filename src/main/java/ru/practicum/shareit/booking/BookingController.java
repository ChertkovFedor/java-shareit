package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bServ;

    @PostMapping
    public BookingDto create(
            @RequestBody BookingCreationDto bookingCreationDto,
            @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bServ.create(userId, bookingCreationDto);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findBookingDtoById(@PathVariable Integer bookingId,
                                         @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bServ.findBookingDtoById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> findAllBookingDtoByBooker(@RequestParam(defaultValue = "ALL") String state,
                                                      @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bServ.findAllBookingDtoByBooker(state, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> findAllBookingDtoByOwner(@RequestParam(defaultValue = "ALL") String state,
                                                     @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bServ.findAllBookingDtoByOwner(state, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@PathVariable Integer bookingId,
                              @RequestParam Boolean approved,
                              @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bServ.approve(bookingId, approved, userId);
    }
}
