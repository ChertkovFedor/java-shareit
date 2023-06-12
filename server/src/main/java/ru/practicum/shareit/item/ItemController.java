package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.ItemCreationDto;

import javax.websocket.server.PathParam;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemService iServ;

    @PostMapping
    public ItemDto create(
            @RequestBody ItemCreationDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        log.info("--server-- POST item {}, ownerId={}", itemDto, ownerId);
        return iServ.create(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(
            @PathVariable(value = "itemId") Integer itemId,
            @RequestBody ItemCreationDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        log.info("--server-- PATCH items {}, ownerId={}, itemId={}", itemDto, ownerId, itemId);
        return iServ.update(itemId, itemDto, ownerId);
    }

    @GetMapping("/all")
    public List<ItemDto> findAll() {
        log.info("--server-- GET all items");
        return iServ.findAll();
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBookingsAndComments getItemById(@PathVariable(value = "itemId") Integer itemId,
                                                      @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("--server-- GET item, itemId={}, userId={}", itemId, userId);
        return iServ.findItemDtoById(itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItemById(@PathVariable(value = "itemId") Integer itemId) {
        log.info("--server-- DELETE item, itemId={}", itemId);
        iServ.deleteItemById(itemId);
        return ResponseEntity.ok("Deleted");
    }

    @GetMapping
    public List<ItemDtoWithBookingsAndComments> findItemsByUser(@RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        log.info("--server-- GET items of owner, ownerId={}", ownerId);
        return iServ.findAllItemDtoByUser(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByNameOrDescription(@PathParam("text") String text,
                                                        @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("--server-- GET search {}, userId={}", text, userId);
        return iServ.searchAllItemDtoByNameOrDescription(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestBody CommentCreateDto commentDto,
                                 @RequestHeader("X-Sharer-User-Id") Integer authorId,
                                 @PathVariable(value = "itemId") Integer itemId) {
        log.info("--server-- POST comment {}, itemId={}, authorId={}", commentDto, itemId, authorId);
        return iServ.addComment(commentDto, authorId, itemId);
    }

}
