package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Getter
@NonNull
public class ItemRequestCreationDto {
    @NotBlank(message = "the request must have description")
    private String description;
}
