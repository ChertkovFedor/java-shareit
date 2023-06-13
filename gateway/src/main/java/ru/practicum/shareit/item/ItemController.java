package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreationDto;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemClient client;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestBody @Valid ItemCreationDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        log.info("--gateway-- POST item {}, ownerId={}", itemDto, ownerId);
        return client.create(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(
            @PathVariable(value = "itemId") Integer itemId,
            @RequestBody @Valid ItemCreationDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        log.info("--gateway-- PATCH items {}, ownerId={}, itemId={}", itemDto, ownerId, itemId);
        return client.update(itemId, itemDto, ownerId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll() {
        log.info("--gateway-- GET all items");
        return client.findAll();
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable(value = "itemId") Integer itemId,
                                              @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("--gateway-- GET item, itemId={}, userId={}", itemId, userId);
        return client.findItemDtoById(itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItemById(@PathVariable(value = "itemId") Integer itemId) {
        log.info("--gateway-- DELETE item, itemId={}", itemId);
        client.deleteItemById(itemId);
        return ResponseEntity.ok("Deleted");
    }

    @GetMapping
    public ResponseEntity<Object> findItemsByUser(@RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        log.info("--gateway-- GET items of owner, ownerId={}", ownerId);
        return client.findAllItemDtoByUser(ownerId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemsByNameOrDescription(@PathParam("text") String text,
                                                                 @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("--gateway-- GET search {}, userId={}", text, userId);
        return client.searchAllItemDtoByNameOrDescription(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestBody CommentCreateDto commentDto,
                                             @RequestHeader("X-Sharer-User-Id") Integer authorId,
                                             @PathVariable Integer itemId) {
        log.info("--gateway-- POST comment {}, itemId={}, authorId={}", commentDto, itemId, authorId);
        return client.addComment(commentDto, authorId, itemId);
    }

}
