package com.mashreq.conference.booking.helper.dto;

import lombok.Data;

@Data
public class BookingRequest {
    private String date;
    private String roomName;
    private String startTime;
    private String endTime;
    private int numberOfPeople;

}

