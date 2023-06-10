package ru.practicum.shareit.booking;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.Status;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookingRequestDbTests {

    @Mock
    private BookingRepository bRep;

    @Test
    public void testFindAllByItemIdIn() {
        List<Integer> itemsId = Arrays.asList(1, 2, 3);
        List<Booking> expectedBookings = Arrays.asList(new Booking(), new Booking());

        when(bRep.findAllByItemIdIn(itemsId)).thenReturn(expectedBookings);

        List<Booking> actualBookings = bRep.findAllByItemIdIn(itemsId);

        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    public void testFindAllByBookerId() {
        Integer userId = 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Booking> expectedBookings = Arrays.asList(new Booking(), new Booking());

        when(bRep.findAllByBookerId(userId, sort, pageRequest)).thenReturn(expectedBookings);

        List<Booking> actualBookings = bRep.findAllByBookerId(userId, sort, pageRequest);

        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    public void testFindAllByBookerIdAndCurrentState() {
        Integer userId = 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Booking> expectedBookings = Arrays.asList(new Booking(), new Booking());

        when(bRep.findAllByBookerIdAndCurrentState(userId, sort, pageRequest)).thenReturn(expectedBookings);

        List<Booking> actualBookings = bRep.findAllByBookerIdAndCurrentState(userId, sort, pageRequest);

        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    public void testFindAllByBookerIdAndFutureState() {
        Integer userId = 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Booking> expectedBookings = Arrays.asList(new Booking(), new Booking());

        when(bRep.findAllByBookerIdAndFutureState(userId, sort, pageRequest)).thenReturn(expectedBookings);

        List<Booking> actualBookings = bRep.findAllByBookerIdAndFutureState(userId, sort, pageRequest);

        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    public void testFindAllByBookerIdAndPastState() {
        Integer userId = 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Booking> expectedBookings = Arrays.asList(new Booking(), new Booking());

        when(bRep.findAllByBookerIdAndPastState(userId, sort, pageRequest)).thenReturn(expectedBookings);

        List<Booking> actualBookings = bRep.findAllByBookerIdAndPastState(userId, sort, pageRequest);

        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    public void testFindAllByBookerIdAndStatus() {
        Integer userId = 1;
        Status status = Status.WAITING;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Booking> expectedBookings = Arrays.asList(new Booking(), new Booking());
        when(bRep.findAllByBookerIdAndStatus(userId, status, sort, pageRequest)).thenReturn(expectedBookings);

        List<Booking> actualBookings = bRep.findAllByBookerIdAndStatus(userId, status, sort, pageRequest);

        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    public void testFindAllByOwnerId() {
        Integer userId = 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Booking> expectedBookings = Arrays.asList(new Booking(), new Booking());

        when(bRep.findAllByOwnerId(userId, sort, pageRequest)).thenReturn(expectedBookings);

        List<Booking> actualBookings = bRep.findAllByOwnerId(userId, sort, pageRequest);

        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    public void testFindAllByOwnerIdAndCurrentState() {
        Integer userId = 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Booking> expectedBookings = Arrays.asList(new Booking(), new Booking());

        when(bRep.findAllByOwnerIdAndCurrentState(userId, sort, pageRequest)).thenReturn(expectedBookings);

        List<Booking> actualBookings = bRep.findAllByOwnerIdAndCurrentState(userId, sort, pageRequest);

        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    public void testFindAllByOwnerIdAndFutureState() {
        Integer userId = 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Booking> expectedBookings = Arrays.asList(new Booking(), new Booking());

        when(bRep.findAllByOwnerIdAndFutureState(userId, sort, pageRequest)).thenReturn(expectedBookings);

        List<Booking> actualBookings = bRep.findAllByOwnerIdAndFutureState(userId, sort, pageRequest);

        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    public void testFindAllByOwnerIdAndPastState() {
        Integer userId = 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Booking> expectedBookings = Arrays.asList(new Booking(), new Booking());

        when(bRep.findAllByOwnerIdAndPastState(userId, sort, pageRequest)).thenReturn(expectedBookings);

        List<Booking> actualBookings = bRep.findAllByOwnerIdAndPastState(userId, sort, pageRequest);

        assertEquals(expectedBookings, actualBookings);
    }

    @Test
    public void testFindAllByOwnerIdAndWaitingOrRejectedState() {
        Integer userId = 1;
        Status status = Status.WAITING;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Booking> expectedBookings = Arrays.asList(new Booking(), new Booking());

        when(bRep.findAllByOwnerIdAndWaitingOrRejectedState(userId, status, sort, pageRequest)).thenReturn(expectedBookings);

        List<Booking> actualBookings = bRep.findAllByOwnerIdAndWaitingOrRejectedState(userId, status, sort, pageRequest);

        assertEquals(expectedBookings, actualBookings);
    }
}
