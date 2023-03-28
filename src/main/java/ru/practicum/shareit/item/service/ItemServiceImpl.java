package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository rep;

    @Override
    public Optional<ItemDto> create(ItemCreationDto itemDto, Integer ownerId) {
        return rep.create(itemDto, ownerId);
    }

    @Override
    public Optional<ItemDto> update(Integer itemId, ItemCreationDto itemDto, Integer ownerId) {
        return rep.update(itemId, itemDto, ownerId);
    }

    @Override
    public Collection<ItemDto> findAll() {
        return rep.findAll();
    }

    @Override
    public Optional<ItemDto> findItemById(Integer itemId) {
        return rep.findItemById(itemId);
    }

    @Override
    public void deleteItemById(Integer itemId) {
        rep.delete(itemId);
    }

    @Override
    public Collection<ItemDto> findItemsByUser(Integer ownerId) {
        return rep.findItemsByUser(ownerId);
    }

    @Override
    public Collection<ItemDto> searchItemsByNameOrDescription(String text) {
        return rep.searchItemsByNameOrDescription(text);
    }

}
