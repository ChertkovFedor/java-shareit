package ru.practicum.shareit.booking;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.status.Role;
import ru.practicum.shareit.booking.status.State;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.booking.validator.BookingValidator;
import ru.practicum.shareit.exception.UserMismatchException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTests {

    @Mock
    BookingRepository bRep;

    @Mock
    ItemService iServ;
    @Mock
    UserService uServ;
    BookingService bServ;

    private final User user1 = new User(
            1,
            "user1",
            "user1@mail.ru");

    private final User user2 = new User(
            2,
            "user2",
            "user2@mail.ru");

    Item item1 = new Item(
            1,
            user1,
            "item1",
            "description1",
            true,
            1
    );

    Item item2 = new Item(
            2,
            user1,
            "item2",
            "description2",
            true,
            1
    );

    BookingCreationDto bookingCreationDto = new BookingCreationDto(
            1,
            LocalDateTime.parse("2023-05-22T19:12:08"),
            LocalDateTime.parse("2023-05-22T19:12:08")
    );

    Booking booking1 = new Booking(
            1,
            item1,
            user1,
            LocalDateTime.parse("2023-05-22T19:12:08"),
            LocalDateTime.parse("2023-05-22T19:12:08"),
            Status.WAITING
    );

    Booking booking2 = new Booking(
            2,
            item2,
            user2,
            LocalDateTime.parse("2023-05-22T19:12:08"),
            LocalDateTime.parse("2023-05-22T19:12:08"),
            Status.APPROVED
    );

    @BeforeEach
    void setUp() {
        bServ = new BookingServiceImpl(iServ, uServ, bRep);
    }

    @Test
    void testCreate() {

        when(iServ.getItemById(any()))
                .thenReturn(item1);

        when(uServ.getUserById(2))
                .thenReturn(user2);

        when(bRep.save(any()))
                .thenReturn(booking2);

        BookingDto bookingDtoNew = bServ.create(2, bookingCreationDto);

        Assertions.assertEquals(2, bookingDtoNew.getId());
        Assertions.assertEquals("2023-05-22T19:12:08", bookingDtoNew.getStart().toString());
        Assertions.assertEquals("2023-05-22T19:12:08", bookingDtoNew.getEnd().toString());
        Assertions.assertEquals(Status.APPROVED, bookingDtoNew.getStatus());
        Assertions.assertEquals(2, bookingDtoNew.getItem().getId());
        Assertions.assertEquals("item2", bookingDtoNew.getItem().getName());
        Assertions.assertEquals("description2", bookingDtoNew.getItem().getDescription());
        Assertions.assertTrue(bookingDtoNew.getItem().getAvailable());
        Assertions.assertEquals(2, bookingDtoNew.getBooker().getId());
        Assertions.assertEquals("user2@mail.ru", bookingDtoNew.getBooker().getEmail());

    }

    @Test
    void testFindBookingsByUser() {

        Booking bookingCurrent = new Booking(
                1,
                item1,
                user1,
                LocalDateTime.now().minusSeconds(10),
                LocalDateTime.now().plusSeconds(10),
                Status.WAITING
        );
        Booking bookingPast = new Booking(
                1,
                item1,
                user1,
                LocalDateTime.now().minusSeconds(20),
                LocalDateTime.now().minusSeconds(10),
                Status.WAITING
        );
        Booking bookingFuture = new Booking(
                1,
                item1,
                user1,
                LocalDateTime.now().plusSeconds(10),
                LocalDateTime.now().plusSeconds(20),
                Status.WAITING
        );
        Booking bookingRejected = new Booking(
                1,
                item1,
                user1,
                LocalDateTime.now().plusSeconds(10),
                LocalDateTime.now().plusSeconds(20),
                Status.REJECTED
        );
        Integer from = 0;
        Integer size = 100;
        List<BookingDto> bookingDtoList;

        when(bRep.findAllByBookerId(any(), any(), any()))
                .thenReturn(List.of(bookingCurrent, bookingPast, bookingFuture));
        bookingDtoList = bServ.findAllBookingDtoByUser(State.ALL, from, size, 1, Role.BOOKER);
        Assertions.assertEquals(3, bookingDtoList.size());

        when(bRep.findAllByBookerIdAndCurrentState(any(), any(), any()))
                .thenReturn(List.of(bookingCurrent));
        bookingDtoList = bServ.findAllBookingDtoByUser(State.CURRENT, from, size, 1, Role.BOOKER);
        Assertions.assertEquals(1, bookingDtoList.size());

        when(bRep.findAllByBookerIdAndPastState(any(), any(), any()))
                .thenReturn(List.of(bookingPast));
        bookingDtoList = bServ.findAllBookingDtoByUser(State.PAST, from, size, 1, Role.BOOKER);
        Assertions.assertEquals(1, bookingDtoList.size());

        when(bRep.findAllByBookerIdAndFutureState(any(), any(), any()))
                .thenReturn(List.of(bookingFuture));
        bookingDtoList = bServ.findAllBookingDtoByUser(State.FUTURE, from, size, 1, Role.BOOKER);
        Assertions.assertEquals(1, bookingDtoList.size());

        when(bRep.findAllByBookerIdAndStatus(any(), any(), any(), any()))
                .thenReturn(List.of(bookingCurrent, bookingPast, bookingFuture));
        bookingDtoList = bServ.findAllBookingDtoByUser(State.WAITING, from, size, 1, Role.BOOKER);
        Assertions.assertEquals(3, bookingDtoList.size());

        when(bRep.findAllByBookerIdAndStatus(any(), any(), any(), any()))
                .thenReturn(List.of(bookingRejected));
        bookingDtoList = bServ.findAllBookingDtoByUser(State.REJECTED, from, size, 1, Role.BOOKER);
        Assertions.assertEquals(1, bookingDtoList.size());


        when(bRep.findAllByOwnerId(any(), any(), any()))
                .thenReturn(List.of(bookingCurrent, bookingPast, bookingFuture));
        bookingDtoList = bServ.findAllBookingDtoByUser(State.ALL, from, size, 1, Role.OWNER);
        Assertions.assertEquals(3, bookingDtoList.size());

        when(bRep.findAllByOwnerIdAndCurrentState(any(), any(), any()))
                .thenReturn(List.of(bookingCurrent));
        bookingDtoList = bServ.findAllBookingDtoByUser(State.CURRENT, from, size, 1, Role.OWNER);
        Assertions.assertEquals(1, bookingDtoList.size());

        when(bRep.findAllByOwnerIdAndPastState(any(), any(), any()))
                .thenReturn(List.of(bookingPast));
        bookingDtoList = bServ.findAllBookingDtoByUser(State.PAST, from, size, 1, Role.OWNER);
        Assertions.assertEquals(1, bookingDtoList.size());

        when(bRep.findAllByOwnerIdAndFutureState(any(), any(), any()))
                .thenReturn(List.of(bookingFuture));
        bookingDtoList = bServ.findAllBookingDtoByUser(State.FUTURE, from, size, 1, Role.OWNER);
        Assertions.assertEquals(1, bookingDtoList.size());

        when(bRep.findAllByOwnerIdAndWaitingOrRejectedState(any(), any(), any(), any()))
                .thenReturn(List.of(bookingCurrent, bookingPast, bookingFuture));
        bookingDtoList = bServ.findAllBookingDtoByUser(State.WAITING, from, size, 1, Role.OWNER);
        Assertions.assertEquals(3, bookingDtoList.size());

        when(bRep.findAllByOwnerIdAndWaitingOrRejectedState(any(), any(), any(), any()))
                .thenReturn(List.of(bookingRejected));
        bookingDtoList = bServ.findAllBookingDtoByUser(State.REJECTED, from, size, 1, Role.OWNER);
        Assertions.assertEquals(1, bookingDtoList.size());

    }

    @Test
    void testBookingForApprove() {

        when(bRep.findById(any()))
                .thenReturn(Optional.ofNullable(booking1));

        when(iServ.getItemById(any()))
                .thenReturn(item1);

        BookingDto bookingDtoApprove = bServ.approve(1, true, 1);

        Assertions.assertEquals(1, bookingDtoApprove.getId());
        Assertions.assertEquals("2023-05-22T19:12:08", bookingDtoApprove.getStart().toString());
        Assertions.assertEquals("2023-05-22T19:12:08", bookingDtoApprove.getEnd().toString());
        Assertions.assertEquals(Status.APPROVED, bookingDtoApprove.getStatus());
        Assertions.assertEquals(1, bookingDtoApprove.getItem().getId());
        Assertions.assertEquals("item1", bookingDtoApprove.getItem().getName());
        Assertions.assertEquals("description1", bookingDtoApprove.getItem().getDescription());
        Assertions.assertTrue(bookingDtoApprove.getItem().getAvailable());
        Assertions.assertEquals(1, bookingDtoApprove.getBooker().getId());
        Assertions.assertEquals("user1@mail.ru", bookingDtoApprove.getBooker().getEmail());

        BookingDto bookingDtoNoApprove = bServ.approve(1, false, 1);

        Assertions.assertEquals(1, bookingDtoNoApprove.getId());
        Assertions.assertEquals("2023-05-22T19:12:08", bookingDtoNoApprove.getStart().toString());
        Assertions.assertEquals("2023-05-22T19:12:08", bookingDtoNoApprove.getEnd().toString());
        Assertions.assertEquals(Status.REJECTED, bookingDtoNoApprove.getStatus());

    }

    @Test
    void testBookingValidate() {

        Item itemNotAvailable = new Item(
                1,
                user1,
                "item",
                "description",
                false,
                1
        );

        Assertions.assertThrows(ValidationException.class,
                () -> BookingValidator.itemBookingApproveValid(booking2, true, item1, 1));

        Assertions.assertThrows(UserMismatchException.class,
                () -> BookingValidator.itemBookingApproveValid(booking2, false, item1, 2));

        Assertions.assertThrows(ValidationException.class,
                () -> BookingValidator.itemBookingCreatValid(itemNotAvailable, 1));

        Assertions.assertThrows(UserMismatchException.class,
                () -> BookingValidator.itemBookingCreatValid(item1, 1));
    }


}