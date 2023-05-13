package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.util.List;

public class ItemRequestMapper {

    public static ItemRequest mapToModel(Integer id, ItemRequestCreationDto itemRequestDto, Instant created, User user) {
        return new ItemRequest(
                id,
                itemRequestDto.getDescription(),
                created,
                user
        );
    }

    public static ItemRequestDto mapToDto(ItemRequest itemRequest, List<ItemDto> items) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                items
        );
    }

//    public static List<ItemRequestDto> mapToListDto(List<ItemRequest> itemRequests) {
//        List<ItemRequestDto> itemRequestsDto = new ArrayList<>();
//        for (ItemRequest ir : itemRequests) {
//            itemRequestsDto.add(mapToDto(ir));
//        }
//        return itemRequestsDto;
//    }

}
