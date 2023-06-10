package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Set;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findItemsByOwnerId(Integer ownerId);

    @Query("select i " +
            "from Item i " +
            "where lower(i.name) like (concat('%', ?1, '%')) " +
            "or lower(i.description) like (concat('%', ?1, '%')) " +
            "and i.available = true")
    List<Item> searchItemsByNameOrDescription(String text);

    @Query("select i " +
            "from Item i " +
            "where i.requestId in ?1")
    List<Item> findAllItemsByRequestIds(Set<Integer> requestIds);
}