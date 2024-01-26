package com.mashreq.conference.booking.Repository;

import com.mashreq.conference.booking.helper.Entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findByRoomNameAndDateAndEndTimeAfterAndStartTimeBefore(
            String roomName, LocalDate date, LocalTime startTime, LocalTime endTime);

    @Query("SELECT b.roomName FROM BookingEntity b " +
            "WHERE b.date = :date " +
            "AND ((b.startTime >= :startTime AND b.startTime < :endTime) " +
            "OR (b.endTime > :startTime AND b.endTime <= :endTime) " +
            "OR (b.startTime <= :startTime AND b.endTime >= :endTime))")
    List<String> findBookedRooms(
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}