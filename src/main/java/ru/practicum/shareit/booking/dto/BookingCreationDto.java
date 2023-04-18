package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingCreationDto {
    @Positive
    @NotNull
    private Integer itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}