package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestDbTests {

    @Mock
    private ItemRequestRepository irRep;

    @Mock
    private UserService uServ;

    @Mock
    private ItemService iServ;

    @InjectMocks
    private ItemRequestServiceImpl irServ;

    @Test
    public void testFindByUserId() {
        Integer userId = 1;
        List<ItemRequest> expectedRequests = new ArrayList<>();
        expectedRequests.add(new ItemRequest(1, "description1", Instant.now(), null));
        expectedRequests.add(new ItemRequest(2, "description2", Instant.now(), null));

        when(irRep.findByUserId(userId))
                .thenReturn(expectedRequests);

        List<ItemRequestDto> result = irServ.findAllRequestDtoByUser(userId);

        assertEquals(expectedRequests.size(), result.size());
        assertEquals(expectedRequests.get(0).getId(), result.get(0).getId());
        assertEquals(expectedRequests.get(0).getDescription(), result.get(0).getDescription());
        assertEquals(expectedRequests.get(0).getCreated(), result.get(0).getCreated());
    }

    @Test
    public void testFindAllExceptUserId() {
        Integer userId = 1;
        Integer from = 0;
        Integer size = 10;
        User user = new User();
        List<ItemRequest> expectedRequests = new ArrayList<>();
        List<ItemDto> items = new ArrayList<>();
        expectedRequests.add(new ItemRequest(1, "description1", Instant.now(), null));
        expectedRequests.add(new ItemRequest(2, "description2", Instant.now(), null));

        PageRequest pageRequest = PageRequest.of(from / size, size);
        when(irRep.findAllExceptUserId(userId, pageRequest))
                .thenReturn(expectedRequests);
        when(uServ.getUserById(userId))
                .thenReturn(user);
        when(iServ.findAllItemsByRequestIds(anySet()))
                .thenReturn(items);

        List<ItemRequestDto> result = irServ.findAllRequestDtoByOthers(userId, from, size);

        assertEquals(expectedRequests.size(), result.size());
        assertEquals(expectedRequests.get(0).getId(), result.get(0).getId());
        assertEquals(expectedRequests.get(0).getDescription(), result.get(0).getDescription());
        assertEquals(expectedRequests.get(0).getCreated(), result.get(0).getCreated());
    }
}
