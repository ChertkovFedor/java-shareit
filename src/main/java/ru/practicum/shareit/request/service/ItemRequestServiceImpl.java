package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.validator.ItemRequestValidator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository irRep;
    private final ItemService iServ;
    private final UserService uServ;

    @Override
    public ItemRequestDto create(ItemRequestCreationDto itemRequestDto, Integer userId) {
        ItemRequestValidator.ItemRequestValid(itemRequestDto);
        User user = uServ.getUserById(userId);
        ItemRequest itemRequest = ItemRequestMapper.mapToModel(null, itemRequestDto, Instant.now(), user);

        return ItemRequestMapper.mapToDto(irRep.save(itemRequest), null);
    }

    @Override
    public ItemRequestDto getRequestDtoById(Integer requestId, Integer userId) {
        uServ.getUserById(userId);
        ItemRequest itemRequest = irRep.findById(requestId).orElseThrow(() ->
                new ObjectNotFoundException(String.format("Request with id %s not found", requestId)));
        List<ItemDto> items = iServ.findAllItemsByRequestIds(Set.of(itemRequest.getId()));

        return ItemRequestMapper.mapToDto(irRep.save(itemRequest), items);
    }

    @Override
    public List<ItemRequestDto> findAllRequestDtoByUser(Integer userId) {
        uServ.getUserById(userId);
        Map<Integer, ItemRequest> requests = irRep.findByUserId(userId).stream()
                .collect(Collectors.toMap(ItemRequest::getId, Function.identity()));

        return getItems(requests);
    }

    @Override
    public List<ItemRequestDto> findAllRequestDtoByOthers(Integer userId, Integer from, Integer size) {
        ItemRequestValidator.PageValid(from, size);
        uServ.getUserById(userId);
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        Map<Integer, ItemRequest> requests = irRep.findAllExceptUserId(userId, page).stream()
                .collect(Collectors.toMap(ItemRequest::getId, Function.identity()));

        return getItems(requests);
    }

    private List<ItemRequestDto> getItems(Map<Integer, ItemRequest> requests) {
        Map<Integer, List<ItemDto>> items = iServ.findAllItemsByRequestIds(requests.keySet()).stream()
                .collect(Collectors.groupingBy(ItemDto::getRequestId));

        return requests.values().stream()
                .sorted(Comparator.comparing(ItemRequest::getCreated).reversed())
                .map(itemRequest -> ItemRequestMapper.mapToDto(itemRequest, items.getOrDefault(itemRequest.getId(), List.of())))
                .collect(Collectors.toList());
    }

}
