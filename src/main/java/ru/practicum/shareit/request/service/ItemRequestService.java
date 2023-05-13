package ru.practicum.shareit.request.service;

import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto create(ItemRequestCreationDto itemRequestDto, Integer userId);

    ItemRequestDto getRequestDtoById(Integer requestId, Integer userId);

    List<ItemRequestDto> findAllRequestDtoByUser(Integer userId);

    List<ItemRequestDto> findAllRequestDtoByOthers(Integer userId, Integer from, Integer size);

}
