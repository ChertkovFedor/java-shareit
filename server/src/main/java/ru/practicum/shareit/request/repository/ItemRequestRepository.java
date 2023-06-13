package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {

    List<ItemRequest> findByUserId(Integer userId);

    @Query("select ir " +
            "from ItemRequest ir " +
            "where ir.user.id not in ?1")
    List<ItemRequest> findAllExceptUserId(Integer userId, PageRequest page);
}
