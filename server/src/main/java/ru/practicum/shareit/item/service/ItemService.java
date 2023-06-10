package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Set;

public interface ItemService {
    ItemDto create(ItemCreationDto itemDto, Integer ownerId);

    ItemDto update(Integer itemId, ItemCreationDto itemDto, Integer ownerId);

    List<ItemDto> findAll();

    ItemDtoWithBookingsAndComments findItemDtoById(Integer itemId, Integer userId);

    void deleteItemById(Integer itemId);

    List<ItemDtoWithBookingsAndComments> findAllItemDtoByUser(Integer ownerId);

    List<ItemDto> searchAllItemDtoByNameOrDescription(String text);

    Item getItemById(Integer itemId);

    CommentDto addComment(CommentCreateDto commentCreationDto, Integer userId, Integer itemId);

    List<ItemDto> findAllItemsByRequestIds(Set<Integer> requestIds);
}
