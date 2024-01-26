package com.mashreq.conference.booking.service.Impl;

import com.mashreq.conference.booking.Repository.BookingRepository;
import com.mashreq.conference.booking.helper.Entity.BookingEntity;
import com.mashreq.conference.booking.helper.properties.ConferenceRoomConfig;
import com.mashreq.conference.booking.helper.properties.MaintenanceTimingsConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ConferenceRoomServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private MaintenanceTimingsConfig maintenanceTimingsConfig;

    @Mock
    private ConferenceRoomConfig conferenceRoomConfig;

    @InjectMocks
    private ConferenceRoomServiceImpl conferenceRoomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAvailableRooms() {
        // Mocking dependencies
        when(conferenceRoomConfig.getRooms()).thenReturn(Arrays.asList(
                new ConferenceRoomConfig.ConferenceRoom("Room1", 10),
                new ConferenceRoomConfig.ConferenceRoom("Room2", 15)
        ));

        when(bookingRepository.findBookedRooms(any(), any(), any())).thenReturn(Collections.singletonList("Room1"));

        List<String> availableRooms = conferenceRoomService.getAvailableRooms("2022-01-01", "09:00:00", "10:00:00");

        assertEquals(Collections.singletonList("Room2"), availableRooms);
    }

    @Test
    void testBookConferenceRoom_SuccessfulBooking() {
        // Mocking dependencies
        when(conferenceRoomConfig.getRooms()).thenReturn(Arrays.asList(
                new ConferenceRoomConfig.ConferenceRoom("Room1", 10),
                new ConferenceRoomConfig.ConferenceRoom("Room2", 15)
        ));

        when(maintenanceTimingsConfig.getTimings()).thenReturn(Collections.singletonList(
                new MaintenanceTimingsConfig.TimeRange("09:00:00", "09:15:00")
        ));

        when(bookingRepository.findByRoomNameAndDateAndEndTimeAfterAndStartTimeBefore(
                anyString(), any(LocalDate.class), any(LocalTime.class), any(LocalTime.class)))
                .thenReturn(Collections.emptyList());

        // Test booking with valid inputs
        String result = conferenceRoomService.bookConferenceRoom("08:00:00", "09:00:00", 5);
        assertEquals("Booking successful! Room: Room1", result);
    }

    @Test
    void testBookConferenceRoom_DuringMaintenance() {
        // Mocking dependencies
        when(maintenanceTimingsConfig.getTimings()).thenReturn(Collections.singletonList(
                new MaintenanceTimingsConfig.TimeRange("09:00:00", "09:15:00")
        ));

        // Test booking during maintenance timings
        String result = conferenceRoomService.bookConferenceRoom("09:05:00", "10:00:00", 5);
        assertEquals("Booking cannot be done during maintenance time.", result);
    }
    @Test
    void testBookConferenceRoom_InvalidTimeRange() {
        // Test booking with invalid time range
        String result = conferenceRoomService.bookConferenceRoom( "12:00:00", "11:00:00", 5);
        assertEquals("Invalid time range.", result);
    }
    @Test
    void testBookConferenceRoom_Intervals() {
        // Test booking with invalid time range
        String result = conferenceRoomService.bookConferenceRoom( "12:10:00", "13:00:00", 5);
        assertEquals("Booking can only be done in intervals of 15 minutes.", result);
    }
    // Additional test cases for various scenarios
}
