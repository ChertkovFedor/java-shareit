package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingForItemDto {
    private Integer id;
    private Item item;
    private Integer bookerId;
    private LocalDateTime start;
    private LocalDateTime end;

    @Data
    @AllArgsConstructor
    public static class Item {
        private Integer id;
        private String name;
    }
}