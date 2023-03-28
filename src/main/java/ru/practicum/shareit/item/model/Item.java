package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Item {

    private final int id;
    private final int ownerId;
    private final String name;
    private final String description;
    private final Boolean available;

}
