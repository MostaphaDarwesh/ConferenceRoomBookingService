package com.mashreq.conference.booking.service.Impl;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mashreq.conference.booking.Repository.BookingRepository;
import com.mashreq.conference.booking.helper.properties.ConferenceRoomConfig;
import com.mashreq.conference.booking.helper.properties.MaintenanceTimingsConfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        // Mocking configurations
        when(conferenceRoomConfig.getRooms()).thenReturn(Arrays.asList(
                new ConferenceRoomConfig.ConferenceRoom("Amaze", 3),
                new ConferenceRoomConfig.ConferenceRoom("Beauty", 7),
                new ConferenceRoomConfig.ConferenceRoom("Inspire", 12)
        ));

        // Mocking booked rooms
        when(bookingRepository.findBookedRooms(any(), any(), any())).thenReturn(Arrays.asList("Amaze", "Inspire"));

        // Testing the method
        List<String> availableRooms = conferenceRoomService.getAvailableRooms("2024-01-01", "09:00:00", "10:00:00");

        // Asserting the result
        assertEquals(Arrays.asList("Beauty"), availableRooms);
    }

    @Test
    void testBookConferenceRoomSuccess() {
        // Mocking configurations
        when(conferenceRoomConfig.getRooms()).thenReturn(Arrays.asList(
                new ConferenceRoomConfig.ConferenceRoom("Amaze", 3),
                new ConferenceRoomConfig.ConferenceRoom("Beauty", 7),
                new ConferenceRoomConfig.ConferenceRoom("Inspire", 12)
        ));

        // Mocking maintenance timings
        when(maintenanceTimingsConfig.getTimings()).thenReturn(Arrays.asList(
                new MaintenanceTimingsConfig.TimeRange("09:00:00", "09:15:00"),
                new MaintenanceTimingsConfig.TimeRange("13:00:00", "13:15:00"),
                new MaintenanceTimingsConfig.TimeRange("17:00:00", "17:15:00")
        ));

        // Mocking bookingRepository behavior
        when(bookingRepository.findByRoomNameAndDateAndEndTimeAfterAndStartTimeBefore(
                any(), any(), any(), any())).thenReturn(new ArrayList<>());

        // Testing the method
        String result = conferenceRoomService.bookConferenceRoom("2024-01-01", "Beauty", "10:00:00", "11:00:00", 5);

        // Asserting the result
        assertEquals("Booking successful!", result);
    }

    @Test
    public void testIsDuringMaintenance() {

                // Mocking maintenance timings
        when(maintenanceTimingsConfig.getTimings()).thenReturn(Arrays.asList(
                new MaintenanceTimingsConfig.TimeRange("09:00:00", "09:15:00"),
                new MaintenanceTimingsConfig.TimeRange("13:00:00", "13:15:00"),
                new MaintenanceTimingsConfig.TimeRange("17:00:00", "17:15:00")
        ));

        // Act & Assert
        assertTrue(conferenceRoomService.isDuringMaintenance(LocalTime.parse("09:10:00")));
        assertTrue(conferenceRoomService.isDuringMaintenance(LocalTime.parse("13:05:00")));
        assertTrue(conferenceRoomService.isDuringMaintenance(LocalTime.parse("17:10:00")));

        assertFalse(conferenceRoomService.isDuringMaintenance(LocalTime.parse("08:59:59")));
        assertFalse(conferenceRoomService.isDuringMaintenance(LocalTime.parse("12:59:59")));
        assertFalse(conferenceRoomService.isDuringMaintenance(LocalTime.parse("16:59:59")));
        assertFalse(conferenceRoomService.isDuringMaintenance(LocalTime.parse("18:00:00")));
    }

    private MaintenanceTimingsConfig createMockMaintenanceTimingsConfig() {
        MaintenanceTimingsConfig maintenanceTimingsConfig = new MaintenanceTimingsConfig();
        maintenanceTimingsConfig.setTimings(List.of(
                new MaintenanceTimingsConfig.TimeRange("09:00:00", "09:15:00"),
                new MaintenanceTimingsConfig.TimeRange("13:00:00", "13:15:00"),
                new MaintenanceTimingsConfig.TimeRange("17:00:00", "17:15:00")
        ));
        return maintenanceTimingsConfig;
    }

    @Test
    void testBookConferenceRoomDuringMaintenance() {
        // Mocking configurations
        when(conferenceRoomConfig.getRooms()).thenReturn(Arrays.asList(
                new ConferenceRoomConfig.ConferenceRoom("Amaze", 3),
                new ConferenceRoomConfig.ConferenceRoom("Beauty", 7),
                new ConferenceRoomConfig.ConferenceRoom("Inspire", 12)
        ));

        // Mocking maintenance timings
        when(maintenanceTimingsConfig.getTimings()).thenReturn(Arrays.asList(
                new MaintenanceTimingsConfig.TimeRange("09:00:00", "09:15:00"),
                new MaintenanceTimingsConfig.TimeRange("13:00:00", "13:15:00"),
                new MaintenanceTimingsConfig.TimeRange("17:00:00", "17:15:00")
        ));

        // Mocking bookingRepository behavior
        when(bookingRepository.findByRoomNameAndDateAndEndTimeAfterAndStartTimeBefore(
                any(), any(), any(), any())).thenReturn(new ArrayList<>());

        // Testing the method
        String result = conferenceRoomService.bookConferenceRoom("2024-01-01", "Beauty", "09:10:00", "09:15:00", 5);

        // Asserting the result
        assertEquals("Booking cannot be done during maintenance time.", result);
    }

    @Test
    void testBookConferenceRoomInvalidRoomName() {
        // Mocking configurations
        when(conferenceRoomConfig.getRooms()).thenReturn(Arrays.asList(
                new ConferenceRoomConfig.ConferenceRoom("Amaze", 3),
                new ConferenceRoomConfig.ConferenceRoom("Beauty", 7),
                new ConferenceRoomConfig.ConferenceRoom("Inspire", 12)
        ));

        // Mocking maintenance timings
        when(maintenanceTimingsConfig.getTimings()).thenReturn(Arrays.asList(
                new MaintenanceTimingsConfig.TimeRange("09:00:00", "09:15:00"),
                new MaintenanceTimingsConfig.TimeRange("13:00:00", "13:15:00"),
                new MaintenanceTimingsConfig.TimeRange("17:00:00", "17:15:00")
        ));

        // Mocking bookingRepository behavior
        when(bookingRepository.findByRoomNameAndDateAndEndTimeAfterAndStartTimeBefore(
                any(), any(), any(), any())).thenReturn(new ArrayList<>());

        // Testing the method
        String result = conferenceRoomService.bookConferenceRoom("2024-01-01", "InvalidRoom", "10:00:00", "11:00:00", 5);

        // Asserting the result
        assertEquals("Invalid room name.", result);
    }
}

// Add more test cases for different scenarios
