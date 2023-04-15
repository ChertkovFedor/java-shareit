package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ITEMS")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID", unique = true, nullable = false, updatable = false)
    private Integer id;
    @JoinColumn(name = "OWNER_ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;
    private String name;
    private String description;
    @Column(name = "IS_AVAILABLE")
    private Boolean available;

}
