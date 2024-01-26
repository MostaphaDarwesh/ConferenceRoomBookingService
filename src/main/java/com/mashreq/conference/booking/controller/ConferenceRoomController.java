package com.mashreq.conference.booking.controller;

import com.mashreq.conference.booking.helper.dto.BookingRequest;
import com.mashreq.conference.booking.service.ConferenceRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class ConferenceRoomController {
    @Autowired
    private ConferenceRoomService conferenceRoomService;

    @PostMapping(value = "/book", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String bookConferenceRoom(@RequestBody BookingRequest bookingRequest) {
        return conferenceRoomService.bookConferenceRoom(
                bookingRequest.getStartTime(),
                bookingRequest.getEndTime(),
                bookingRequest.getNumberOfPeople()
        );
    }

    @GetMapping("/available")
    public ResponseEntity<List<String>> getAvailableRooms(
            @RequestHeader String date,
            @RequestHeader String startTime,
            @RequestHeader String endTime) {

        try {
            List<String> availableRooms = conferenceRoomService.getAvailableRooms(date, startTime, endTime);
            return ResponseEntity.ok(availableRooms);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }
}


