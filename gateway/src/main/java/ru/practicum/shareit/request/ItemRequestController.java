package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ItemRequestCreationDto itemRequestDto,
                                         @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("POST item request {}, userId={}", itemRequestDto, userId);
        return client.create(itemRequestDto, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestDtoById(@PathVariable Integer requestId,
                                            @RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("GET items request of user, userId={}, requestId={}", userId, requestId);
        return client.getRequestDtoById(requestId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllRequestDtoByUser(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("GET items request of user, userId={}", userId);
        return client.findAllRequestDtoByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllRequestDtoByOthers(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                          @RequestParam(required = false, defaultValue = "0") Integer from,
                                                          @RequestParam(required = false, defaultValue = "20") Integer size) {
        log.info("GET all item requests of others, userId={}, from={}, size={}", userId, from, size);
        return client.findAllRequestDtoByOthers(userId, from, size);
    }

}
