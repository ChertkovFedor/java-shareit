package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService irServ;

    @PostMapping
    public ItemRequestDto create(@RequestBody ItemRequestCreationDto itemRequestDto,
                                 @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return irServ.create(itemRequestDto, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestDtoById(@PathVariable Integer requestId,
                                            @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return irServ.getRequestDtoById(requestId, userId);
    }

    @GetMapping
    public List<ItemRequestDto> findAllRequestDtoByUser(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return irServ.findAllRequestDtoByUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllRequestDtoByOthers(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                          @RequestParam(required = false, defaultValue = "0") Integer from,
                                                          @RequestParam(required = false, defaultValue = "20") Integer size) {
        return irServ.findAllRequestDtoByOthers(userId, from, size);
    }

}
