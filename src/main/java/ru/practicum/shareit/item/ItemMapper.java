package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.Map;

public class ItemMapper {

    public static Item mapToModel(int id, ItemCreationDto itemDto, int ownerId) {
        return new Item(
                id,
                ownerId,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable()
        );
    }

    public static ItemDto mapToDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static Map<Integer, ItemDto> mapToItemsDto(Map<Integer, Item> items) {
        Map<Integer, ItemDto> itemsDto = new HashMap<>();
        for (Item item : items.values()) {
            ItemDto itemDto = ItemMapper.mapToDto(item);
            itemsDto.put(itemDto.getId(), itemDto);
        }
        return itemsDto;
    }

}
