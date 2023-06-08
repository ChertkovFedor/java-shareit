package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BOOKINGS")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKING_ID", unique = true, nullable = false, updatable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM_ID")
    private Item item;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BOOKER_ID")
    private User booker;
    @Column(name = "START_DATE")
    private LocalDateTime start;
    @Column(name = "END_DATE")
    private LocalDateTime end;
    @Enumerated(EnumType.STRING)
    private Status status;
}