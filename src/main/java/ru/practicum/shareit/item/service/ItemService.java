package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.Optional;

public interface ItemService {
    Optional<ItemDto> create(ItemCreationDto itemDto, Integer ownerId);
    Optional<ItemDto> update(Integer itemId, ItemCreationDto itemDto, Integer ownerId);
    Collection<ItemDto> findAll();
    Optional<ItemDto> findItemById(Integer itemId);
    void deleteItemById(Integer itemId);
    Collection<ItemDto> findItemsByUser(Integer ownerId);
    Collection<ItemDto> searchItemsByNameOrDescription(String text);
}
