package ru.practicum.shareit.booking.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingForItemDto {
    @Positive
    private Integer id;
    @NotNull
    private Item item;
    @Positive
    private Integer bookerId;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;

    @Data
    @AllArgsConstructor
    public static class Item {
        private Integer id;
        private String name;
    }
}
