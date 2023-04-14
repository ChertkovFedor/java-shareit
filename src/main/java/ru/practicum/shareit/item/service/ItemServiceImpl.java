package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.validator.ItemValidator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository iRep;
    private final UserService uServ;
    private final CommentService cServ;
    private final BookingRepository bRep;

    @Override
    public ItemDto create(ItemCreationDto itemDto, Integer ownerId) {
        ItemValidator.itemValid(itemDto);
        User owner = uServ.getUserById(ownerId);
        Item item = ItemMapper.mapToModel(null, itemDto, owner);

        return ItemMapper.mapToDto(iRep.save(item));
    }

    @Override
    public ItemDto update(Integer itemId, ItemCreationDto itemDto, Integer ownerId) {
        User owner = uServ.getUserById(ownerId);
        Item item = getItemById(itemId);
        ItemValidator.checkingOwner(item, ownerId);
        if (itemDto.getName() == null)
            itemDto.setName(item.getName());
        if (itemDto.getDescription() == null)
            itemDto.setDescription(item.getDescription());
        if (itemDto.getAvailable() == null)
            itemDto.setAvailable(item.getAvailable());
        ItemValidator.itemValid(itemDto);
        item = ItemMapper.mapToModel(itemId, itemDto, owner);

        return ItemMapper.mapToDto(iRep.save(item));
    }

    @Override
    public List<ItemDto> findAll() {
        return ItemMapper.mapToListDto(iRep.findAll());
    }

    @Override
    public ItemDtoWithBookingsAndComments findItemDtoById(Integer itemId, Integer userId) {
        uServ.getUserById(userId);
        Item item = getItemById(itemId);
        List<Item> list = List.of(item);
        ItemDtoWithBookingsAndComments itemDtoWithBookingsAndComments = addParametersForItems(list).get(0);
        if (!userId.equals(item.getOwner().getId())) {
            itemDtoWithBookingsAndComments.setLastBooking(null);
            itemDtoWithBookingsAndComments.setNextBooking(null);
        }

        return itemDtoWithBookingsAndComments;
    }

    @Override
    public void deleteItemById(Integer itemId) {
        iRep.delete(getItemById(itemId));
    }

    @Override
    public List<ItemDtoWithBookingsAndComments> findAllItemDtoByUser(Integer ownerId) {
        uServ.findUserDtoById(ownerId);
        List<Item> items = iRep.findItemsByOwnerId(ownerId);

        return addParametersForItems(items);
    }

    @Override
    public List<ItemDto> searchAllItemDtoByNameOrDescription(String text) {
        if (text.isBlank())
            return List.of();
        List<Item> items = iRep.searchItemsByNameOrDescription(text.toLowerCase());

        return ItemMapper.mapToListDto(items);
    }

    @Override
    public CommentDto addComment(CommentCreateDto commentCreateDto, Integer authorId, Integer itemId) {
        ItemValidator.commentValid(commentCreateDto);
        User author = uServ.getUserById(authorId);
        List<Booking> pastBookings = bRep.findAllByBookerIdAndPastState(authorId, Sort.by(Sort.Direction.DESC, "end"));
        ItemValidator.checkingPastBooking(pastBookings, authorId);
        Item item = pastBookings.stream()
                .map(Booking::getItem)
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow();
        Comment comment = CommentMapper.mapToModel(null, commentCreateDto, item, author, Instant.now());

        return cServ.create(comment);
    }

    @Override
    public Item getItemById(Integer itemId) {
        return iRep.findById(itemId).orElseThrow(() ->
                new ObjectNotFoundException(String.format("Item with id %s not found", itemId)));
    }

    private List<ItemDtoWithBookingsAndComments> addParametersForItems(List<Item> items) {
        List<Integer> itemsId = items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        List<BookingForItemDto> bookings = bRep.findAllByItemIdIn(itemsId).stream()
                .filter(b -> !b.getStatus().equals(Status.REJECTED))
                .map(BookingMapper::mapToDtoForItem)
                .collect(Collectors.toList());

        List<Comment> comments = cServ.getAllCommentsByItemIdIn(itemsId);

        Set<ItemDtoWithBookingsAndComments> itemsWithBookings =
                new TreeSet<>(Comparator.comparing(ItemDtoWithBookingsAndComments::getId));
        for (Item item : items) {
            Set<BookingForItemDto> nextBookings = new TreeSet<>(Comparator.comparing(BookingForItemDto::getStart));
            Set<BookingForItemDto> lastBookings = new TreeSet<>(Comparator.comparing(BookingForItemDto::getEnd).reversed());
            for (BookingForItemDto booking : bookings) {
                LocalDateTime now = LocalDateTime.now();
                if (item.getId().equals(booking.getItem().getId())) {
                    if (booking.getStart().isAfter(now)) {
                        nextBookings.add(booking);
                    } else {
                        lastBookings.add(booking);
                    }
                }
            }
            ItemDtoWithBookingsAndComments itemDtoWithBookings;
            if (comments.isEmpty()) {
                itemDtoWithBookings = ItemMapper.mapToDtoWithBookingsAndComments(
                        item,
                        lastBookings.stream().findFirst().orElse(null),
                        nextBookings.stream().findFirst().orElse(null),
                        Collections.emptyList());
                itemsWithBookings.add(itemDtoWithBookings);
                continue;
            }

            List<CommentDto> commentsDto = new ArrayList<>();
            for (Comment comment : comments) {
                if (comment.getItem().getId().equals(item.getId())) {
                    commentsDto.add(CommentMapper.mapToDto(comment));
                }
            }
            itemDtoWithBookings = ItemMapper.mapToDtoWithBookingsAndComments(
                    item,
                    lastBookings.stream().findFirst().orElse(null),
                    nextBookings.stream().findFirst().orElse(null),
                    commentsDto);
            itemsWithBookings.add(itemDtoWithBookings);
        }

        return new ArrayList<>(itemsWithBookings);
    }

}
