package com.mashreq.conference.booking.helper.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@Table(name = "bookings")
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "room_name")
    private String roomName;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "start_time")
    private LocalTime startTime;
    @Column(name = "end_time")
    private LocalTime endTime;
    @Column(name = "number_Of_People")
    private int numberOfPeople;

    public BookingEntity() {

    }

    public BookingEntity(LocalDate date, String room, LocalTime startTime, LocalTime endTime, int numberOfPeople) {
        this.date = date;
        this.roomName = room;
        this.startTime = LocalTime.from(startTime);
        this.endTime = LocalTime.from(endTime);
        this.numberOfPeople = numberOfPeople;
    }
}
