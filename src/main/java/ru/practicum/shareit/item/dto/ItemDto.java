package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ItemDto {
    private final int id;
    private final String name;
    private final String description;
    private final boolean available;
}
