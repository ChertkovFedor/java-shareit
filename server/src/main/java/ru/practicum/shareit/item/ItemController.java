package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.ItemCreationDto;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemService iServ;

    @PostMapping
    public ItemDto create(
            @RequestBody ItemCreationDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        return iServ.create(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(
            @PathVariable(value = "itemId") Integer itemId,
            @RequestBody ItemCreationDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        return iServ.update(itemId, itemDto, ownerId);
    }

    @GetMapping("/all")
    public List<ItemDto> findAll() {
        return iServ.findAll();
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBookingsAndComments getItemById(@PathVariable(value = "itemId") Integer itemId,
                                                      @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return iServ.findItemDtoById(itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItemById(@PathVariable(value = "itemId") Integer itemId) {
        iServ.deleteItemById(itemId);
    }

    @GetMapping
    public List<ItemDtoWithBookingsAndComments> findItemsByUser(@RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        return iServ.findAllItemDtoByUser(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByNameOrDescription(@PathParam("text") String text) {
        return iServ.searchAllItemDtoByNameOrDescription(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestBody CommentCreateDto commentCreationDto,
                                 @RequestHeader("X-Sharer-User-Id") Integer authorId,
                                 @PathVariable Integer itemId) {
        return iServ.addComment(commentCreationDto, authorId, itemId);
    }

}
