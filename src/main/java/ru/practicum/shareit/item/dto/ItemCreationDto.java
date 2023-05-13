package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NonNull
public class ItemCreationDto {
    @Size(max = 50)
    private String name;
    @Size(max = 255)
    private String description;
    private Boolean available;
    private Integer requestId;
}
