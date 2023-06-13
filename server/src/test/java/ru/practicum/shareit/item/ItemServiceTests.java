package ru.practicum.shareit.item;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTests {

    @Mock
    ItemRepository iRep;

    @Mock
    BookingRepository bRep;

    @Mock
    CommentService cServ;

    @Mock
    UserService uServ;

    ItemService iServ;

    private final User user1 = new User(
            1,
            "user1",
            "user1@mail.ru");

    private final UserDto userDto = new UserDto(
            1,
            "user1",
            "user@mail.ru");

    ItemCreationDto itemCreationDto = new ItemCreationDto(
            "item1",
            "description1",
            true,
            1);

    Item item1New = new Item(
            null,
            user1,
            "item1",
            "description1",
            true,
            1
    );

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

    Booking booking1 = new Booking(
            1,
            item1,
            user1,
            LocalDateTime.parse("2023-04-22T19:12:08"),
            LocalDateTime.parse("2023-04-22T19:12:08"),
            Status.WAITING
    );

    Booking booking2 = new Booking(
            2,
            item1,
            user1,
            LocalDateTime.parse("2023-04-22T19:12:08"),
            LocalDateTime.parse("2023-04-22T19:12:08"),
            Status.CANCELED
    );

    @BeforeEach
    void setUp() {
        iServ = new ItemServiceImpl(iRep, uServ, cServ, bRep);
    }

    @Test
    void testCreateItem() {

        when(iRep.save(item1New))
                .thenReturn(item1);

        when(uServ.getUserById(1))
                .thenReturn(user1);

        ItemDto itemDto = iServ.create(itemCreationDto, 1);

        Mockito.verify(uServ, Mockito.times(1))
                .getUserById(1);

        Assertions.assertEquals(1, itemDto.getId());
        Assertions.assertEquals("item1", itemDto.getName());
        Assertions.assertEquals("description1", itemDto.getDescription());
        Assertions.assertEquals(true, itemDto.getAvailable());
        Assertions.assertEquals(1, itemDto.getRequestId());
    }

    @Test
    void testUpdateItem() {

        when(uServ.getUserById(1))
                .thenReturn(user1);

        when(iRep.findById(1))
                .thenReturn(Optional.ofNullable(item1));

        when(iRep.save(item1))
                .thenReturn(item1);

        ItemDto itemDto = iServ.update(1, itemCreationDto, 1);

        Mockito.verify(uServ, Mockito.times(1))
                .getUserById(1);

        Assertions.assertEquals(1, itemDto.getId());
        Assertions.assertEquals("item1", itemDto.getName());
        Assertions.assertEquals("description1", itemDto.getDescription());
        Assertions.assertEquals(true, itemDto.getAvailable());
        Assertions.assertEquals(1, itemDto.getRequestId());
    }

    @Test
    void testSearch() {

        when(iRep.searchItemsByNameOrDescription("item"))
                .thenReturn(List.of(item1, item2));

        List<ItemDto> itemDtoList = iServ.searchAllItemDtoByNameOrDescription("item", 1);

        Assertions.assertEquals(2, itemDtoList.size());
        Assertions.assertEquals(1, itemDtoList.get(0).getId());
        Assertions.assertEquals("item1", itemDtoList.get(0).getName());
        Assertions.assertEquals("description1", itemDtoList.get(0).getDescription());
        Assertions.assertEquals(true, itemDtoList.get(0).getAvailable());
        Assertions.assertEquals(1, itemDtoList.get(0).getRequestId());
    }

    @Test
    void testFindById() {

        when(iRep.findById(1))
                .thenReturn(Optional.ofNullable(item1));

        ItemDtoWithBookingsAndComments itemDto = iServ.findItemDtoById(1, 1);

        Mockito.verify(uServ, Mockito.times(1))
                .getUserById(1);

        Assertions.assertEquals(1, itemDto.getId());
        Assertions.assertEquals("item1", itemDto.getName());
        Assertions.assertEquals("description1", itemDto.getDescription());
        Assertions.assertEquals(true, itemDto.getAvailable());
        Assertions.assertNull(itemDto.getLastBooking());
        Assertions.assertNull(itemDto.getNextBooking());

        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> iServ.findItemDtoById(2, 1));

        Assertions.assertEquals("Item with id 2 not found", exception.getMessage());

    }

    @Test
    public void testAddComment() {

        when(uServ.getUserById(1))
                .thenReturn(user1);

        when(bRep.findAllByBookerIdAndPastState(1, Sort.by(Sort.Direction.DESC, "end"), null))
                .thenReturn(List.of(booking1, booking2));


        CommentCreateDto commentCreateDto = new CommentCreateDto("text");

        CommentDto commentDto = new CommentDto(
                1,
                "text",
                "user1",
                Instant.now());

        when(cServ.create(argThat(comment -> comment.getText().equals("text"))))
                .thenReturn(commentDto);

        CommentDto commentDto1 = iServ.addComment(commentCreateDto, 1, 1);

        Assertions.assertEquals(1, commentDto1.getId());
        Assertions.assertEquals(commentCreateDto.getText(), commentDto1.getText());
        Assertions.assertEquals(userDto.getName(), commentDto1.getAuthorName());
        Assertions.assertNotNull(commentDto1.getCreated());

        when(bRep.findAllByBookerIdAndPastState(eq(1), any(), any()))
                .thenReturn(List.of());

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> iServ.addComment(commentCreateDto, 1, 1));

        Assertions.assertEquals("The item was not booked", exception.getMessage());
    }
}