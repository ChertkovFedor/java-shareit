package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.websocket.server.PathParam;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public Optional<ItemDto> create(
            @RequestBody ItemCreationDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        return itemService.create(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public Optional<ItemDto> update(
            @PathVariable(value = "itemId") Integer itemId,
            @RequestBody ItemCreationDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        return itemService.update(itemId, itemDto, ownerId);
    }

    @GetMapping("/all")
    public Collection<ItemDto> findAll() {
        return itemService.findAll();
    }

    @GetMapping("/{itemId}")
    public Optional<ItemDto> getItemById(@PathVariable(value = "itemId") Integer itemId) {
        return itemService.findItemById(itemId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItemById(@PathVariable(value = "itemId") Integer itemId) {
        itemService.deleteItemById(itemId);
    }

    @GetMapping
    public Collection<ItemDto> findItemsByUser(@RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        return itemService.findItemsByUser(ownerId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItemsByNameOrDescription(@PathParam("text") String text) {
        return itemService.searchItemsByNameOrDescription(text);
    }

}
