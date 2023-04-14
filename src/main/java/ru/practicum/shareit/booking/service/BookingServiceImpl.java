package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.State;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.booking.validator.BookingValidator;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.StateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final ItemService iServ;
    private final UserService uServ;
    private final BookingRepository bRep;

    @Override
    public BookingDto create(Integer userId, BookingCreationDto bookingDto) {
        BookingValidator.bookingValid(bookingDto);
        Item item = iServ.getItemById(bookingDto.getItemId());
        BookingValidator.itemBookingCreatValid(item, userId);
        User user = uServ.getUserById(userId);
        Booking booking = BookingMapper.mapToModel(null, bookingDto, item, user, Status.WAITING);
        return BookingMapper.mapToDto(bRep.save(booking));
    }

    @Override
    public BookingDto findBookingDtoById(Integer bookingId, Integer userId) {
        Booking booking = getBookingById(bookingId);
        BookingValidator.checkingViewer(booking, userId);
        return BookingMapper.mapToDto(booking);
    }

    @Override
    public Booking getBookingById(Integer bookingId) {
        return bRep.findById(bookingId).orElseThrow(() ->
                new ObjectNotFoundException(String.format("Booking with id %s not found", bookingId)));
    }

    @Override
    public List<BookingDto> findAllBookingDtoByBooker(String stateTitle, Integer userId) {
        State state = getState(stateTitle);
        uServ.getUserById(userId);
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings = bRep.findAllByBookerId(userId, Sort.by(Sort.Direction.DESC, "end"));
                break;
            case CURRENT:
                bookings = bRep.findAllByBookerIdAndCurrentState(userId, Sort.by(Sort.Direction.DESC, "end"));
                break;
            case FUTURE:
                bookings = bRep.findAllByBookerIdAndFutureState(userId, Sort.by(Sort.Direction.DESC, "end"));
                break;
            case PAST:
                bookings = bRep.findAllByBookerIdAndPastState(userId, Sort.by(Sort.Direction.DESC, "end"));
                break;
            case WAITING:
                bookings = bRep.findAllByBookerIdAndStatus(userId, Status.WAITING, Sort.by(Sort.Direction.DESC, "end"));
                break;
            case REJECTED:
                bookings = bRep.findAllByBookerIdAndStatus(userId, Status.REJECTED, Sort.by(Sort.Direction.DESC, "end"));
                break;
        }
        return BookingMapper.mapToListDto(bookings);
    }

    @Override
    public List<BookingDto> findAllBookingDtoByOwner(String stateTitle, Integer userId) {
        State state = getState(stateTitle);
        uServ.getUserById(userId);
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings = bRep.findAllByOwnerId(userId, Sort.by(Sort.Direction.DESC, "end"));
                break;
            case CURRENT:
                bookings = bRep.findAllByOwnerIdAndCurrentState(userId, Sort.by(Sort.Direction.DESC, "end"));
                break;
            case FUTURE:
                bookings = bRep.findAllByOwnerIdAndFutureState(userId, Sort.by(Sort.Direction.DESC, "end"));
                break;
            case PAST:
                bookings = bRep.findAllByOwnerIdAndPastState(userId, Sort.by(Sort.Direction.DESC, "end"));
                break;
            case WAITING:
                bookings = bRep.findAllByOwnerIdAndWaitingOrRejectedState(userId, Status.WAITING,Sort.by(Sort.Direction.DESC, "end"));
                break;
            case REJECTED:
                bookings = bRep.findAllByOwnerIdAndWaitingOrRejectedState(userId, Status.REJECTED, Sort.by(Sort.Direction.DESC, "end"));
                break;
        }
        return BookingMapper.mapToListDto(bookings);
    }

    @Override
    public BookingDto approve(Integer bookingId, Boolean approved, Integer userId) {
        Booking booking = getBookingById(bookingId);
        Item item = iServ.getItemById(booking.getItem().getId());
        BookingValidator.itemBookingApproveValid(booking, approved, item, userId);
        if (approved)
            booking.setStatus(Status.APPROVED);
        else
            booking.setStatus(Status.REJECTED);
        return BookingMapper.mapToDto(booking);
    }

    @Override
    public List<Booking> getAllBookingByItemIdIn(List<Integer> itemsId) {
        return bRep.findAllByItemIdIn(itemsId);
    }

    private State getState(String state) {
        try {
            return State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new StateException(String.format("Unknown state: %s", state));
        }
    }
}