package com.mashreq.conference.booking.service;

import java.util.List;

public interface ConferenceRoomService {

    List<String> getAvailableRooms(String date, String startTime, String endTime);

    String bookConferenceRoom(String date, String roomName, String startTime, String endTime, int numberOfPeople);
}


