package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequestDto {
    private Integer id;
    @NotBlank(message = "the request must have description")
    private String description;
    private Instant created;
    private List<ItemDto> items;
}
