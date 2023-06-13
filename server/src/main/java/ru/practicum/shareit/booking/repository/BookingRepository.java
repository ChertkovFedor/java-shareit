package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByItemIdIn(List<Integer> itemsId);

    List<Booking> findAllByBookerId(Integer userId, Sort sort, PageRequest page);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.booker.id = ?1 AND CURRENT_TIMESTAMP BETWEEN b.start AND b.end")
    List<Booking> findAllByBookerIdAndCurrentState(Integer userId, Sort sort, PageRequest page);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.booker.id = ?1 AND b.start > CURRENT_TIMESTAMP")
    List<Booking> findAllByBookerIdAndFutureState(Integer userId, Sort sort, PageRequest page);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.booker.id = ?1 AND b.end < CURRENT_TIMESTAMP")
    List<Booking> findAllByBookerIdAndPastState(Integer userId, Sort sort, PageRequest page);

    List<Booking> findAllByBookerIdAndStatus(Integer userId, Status status, Sort sort, PageRequest page);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.owner.id = ?1")
    List<Booking> findAllByOwnerId(Integer userId, Sort sort, PageRequest page);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.owner.id = ?1 AND CURRENT_TIMESTAMP BETWEEN b.start AND b.end")
    List<Booking> findAllByOwnerIdAndCurrentState(Integer userId, Sort sort, PageRequest page);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.owner.id = ?1 AND b.start > CURRENT_TIMESTAMP")
    List<Booking> findAllByOwnerIdAndFutureState(Integer userId, Sort sort, PageRequest page);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.owner.id = ?1 AND b.end < CURRENT_TIMESTAMP")
    List<Booking> findAllByOwnerIdAndPastState(Integer userId, Sort sort, PageRequest page);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.owner.id = ?1 AND b.status = ?2")
    List<Booking> findAllByOwnerIdAndWaitingOrRejectedState(Integer userId, Status status, Sort sort, PageRequest page);

}
