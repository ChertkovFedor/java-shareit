package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.validator.ItemValidator;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {

    private final UserRepository userRepository;
    private final Map<Integer, Item> items = new HashMap<>();
    private int idGenerator = 1;

    public ItemRepositoryImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<ItemDto> create(ItemCreationDto itemDto, Integer ownerId) {
        ItemValidator.itemValid(itemDto);
        userRepository.findUserById(ownerId);
        Item item = ItemMapper.mapToModel(idGenerator, itemDto, ownerId);
        idGenerator++;
        items.put(item.getId(), item);
        log.info("Item added: {}", item);
        return findItemById(item.getId());
    }

    @Override
    public Optional<ItemDto> update(Integer itemId, ItemCreationDto itemDto, Integer ownerId) {
        userRepository.findUserById(ownerId);
        if (items.get(itemId).getOwnerId() != ownerId)
            throw new ObjectNotFoundException(String.format("Owner with id %s not found", ownerId));
        Item item = items.get(itemId);
        if (itemDto.getName() == null)
            itemDto.setName(item.getName());
        if (itemDto.getDescription() == null)
            itemDto.setDescription(item.getDescription());
        if (itemDto.getAvailable() == null)
            itemDto.setAvailable(item.getAvailable());
        item = ItemMapper.mapToModel(itemId, itemDto, ownerId);
        ItemValidator.itemValid(itemDto);
        items.put(item.getId(), item);
        log.info("Item updated: {}", item);
        return findItemById(item.getId());
    }

    @Override
    public Collection<ItemDto> findAll() {
        return ItemMapper.mapToItemsDto(items).values();
    }

    @Override
    public Optional<ItemDto> findItemById(Integer itemId) {
        if (!items.containsKey(itemId)) throw new ObjectNotFoundException(
                String.format("Item with id %s not found", itemId));
        return Optional.of(ItemMapper.mapToDto(items.get(itemId)));
    }

    @Override
    public void delete(Integer itemId) {
        items.remove(itemId, items.get(itemId));
        log.info("Item deleted: {}", itemId);
    }

    @Override
    public Collection<ItemDto> findItemsByUser(Integer ownerId) {
        userRepository.findUserById(ownerId);
        Map<Integer, Item> itemsByUser = new HashMap<>();
        for (Item item : items.values()) {
            if (item.getOwnerId() == ownerId)
                itemsByUser.put(item.getId(), item);
        }
        return ItemMapper.mapToItemsDto(itemsByUser).values();
    }

    @Override
    public Collection<ItemDto> searchItemsByNameOrDescription(String text) {
        Map<Integer, Item> foundItems = new HashMap<>();
        if (!text.isEmpty())
            foundItems = items.values().stream()
                    .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .filter(Item::getAvailable)
                    .collect(Collectors.toMap(
                            Item::getId,
                            item -> item));

        return ItemMapper.mapToItemsDto(foundItems).values();
    }


}
