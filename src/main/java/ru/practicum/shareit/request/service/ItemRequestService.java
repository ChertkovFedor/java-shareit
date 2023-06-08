package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto create(ItemRequestCreationDto itemRequestDto, Integer userId);

    ItemRequestDto getRequestDtoById(Integer requestId, Integer userId);

    List<ItemRequestDto> findAllRequestDtoByUser(Integer userId);

    List<ItemRequestDto> findAllRequestDtoByOthers(Integer userId, Integer from, Integer size);

}
