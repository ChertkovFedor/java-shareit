package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTests {

    @Mock
    private ItemRequestRepository irRep;
    @Mock
    private ItemService iServ;
    @Mock
    private UserService uServ;

    private ItemRequestService irServ;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        irServ = new ItemRequestServiceImpl(irRep, iServ, uServ);
    }

    @Test
    void testCreate() {
        
        ItemRequestCreationDto itemRequestDto = new ItemRequestCreationDto("description");
        Integer userId = 1;
        User user = new User();
        when(uServ.getUserById(userId)).thenReturn(user);
        ItemRequest itemRequest = new ItemRequest(1, "description", Instant.now(), user);
        when(irRep.save(any(ItemRequest.class))).thenReturn(itemRequest);
        ItemRequestDto expectedDto = new ItemRequestDto(1, "description", Instant.now(), null);

        
        ItemRequestDto result = irServ.create(itemRequestDto, userId);

        
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getDescription(), result.getDescription());
        assertEquals(expectedDto.getItems(), result.getItems());
        assertEquals(expectedDto.getCreated().toEpochMilli(), result.getCreated().toEpochMilli(), 1000);
        verify(uServ, times(1)).getUserById(userId);
        verify(irRep, times(1)).save(argThat(item -> item.getDescription().equals("description")));
    }


    @Test
    void testGetRequestDtoById() {
        
        Integer requestId = 1;
        Integer userId = 1;
        User user = new User();
        Instant expectedTime = Instant.parse("2023-05-14T08:12:36.637108200Z");
        ItemRequest itemRequest = new ItemRequest(requestId, "description", expectedTime, user);
        List<ItemDto> items = new ArrayList<>();
        ItemRequestDto expectedDto = new ItemRequestDto(requestId, "description", expectedTime, items);

        when(uServ.getUserById(userId)).thenReturn(user);

        when(irRep.findById(requestId)).thenReturn(Optional.of(itemRequest));

        when(iServ.findAllItemsByRequestIds(anySet())).thenReturn(items);

        when(irRep.save(any())).thenReturn(itemRequest);

        
        ItemRequestDto result = irServ.getRequestDtoById(requestId, userId);

        
        assertEquals(expectedDto, result);
        verify(uServ, times(1)).getUserById(userId);
        verify(irRep, times(1)).findById(requestId);
        verify(iServ, times(1)).findAllItemsByRequestIds(anySet());
        verify(irRep, times(1)).save(argThat(request -> request.getCreated().equals(expectedTime)));
    }


    @Test
    void testFindAllRequestDtoByUser() {
        
        Integer userId = 1;
        User user = new User();
        when(uServ.getUserById(userId)).thenReturn(user);
        Instant now = Instant.now();
        ItemRequest itemRequest1 = new ItemRequest(1, "description1", now, user);
        ItemRequest itemRequest2 = new ItemRequest(2, "description2", now, user);
        List<ItemRequest> itemRequests = List.of(itemRequest1, itemRequest2);
        when(irRep.findByUserId(userId)).thenReturn(itemRequests);
        Set<Integer> requestIds = new HashSet<>();
        requestIds.add(1);
        requestIds.add(2);
        List<ItemDto> items = new ArrayList<>();
        when(iServ.findAllItemsByRequestIds(requestIds)).thenReturn(items);
        List<ItemRequestDto> expectedDtoList = List.of(
                new ItemRequestDto(1, "description1", now, items),
                new ItemRequestDto(2, "description2", now, items)
        );

        
        List<ItemRequestDto> result = irServ.findAllRequestDtoByUser(userId);

        
        assertEquals(expectedDtoList, result);
        verify(uServ, times(1)).getUserById(userId);
        verify(irRep, times(1)).findByUserId(userId);
        verify(iServ, times(1)).findAllItemsByRequestIds(requestIds);
    }


    @Test
    void testFindAllRequestDtoByOthers() {
        
        Integer userId = 1;
        Integer from = 0;
        Integer size = 20;
        User user = new User();
        when(uServ.getUserById(userId)).thenReturn(user);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<ItemRequest> itemRequests = new ArrayList<>();
        when(irRep.findAllExceptUserId(userId, pageRequest)).thenReturn(itemRequests);
        List<ItemDto> items = new ArrayList<>();
        when(iServ.findAllItemsByRequestIds(anySet())).thenReturn(items);
        List<ItemRequestDto> expectedDtoList = new ArrayList<>();

        
        List<ItemRequestDto> result = irServ.findAllRequestDtoByOthers(userId, from, size);

        
        assertEquals(expectedDtoList, result);
        verify(uServ, times(1)).getUserById(userId);
        verify(irRep, times(1)).findAllExceptUserId(userId, pageRequest);
        verify(iServ, times(1)).findAllItemsByRequestIds(anySet());
    }

}

