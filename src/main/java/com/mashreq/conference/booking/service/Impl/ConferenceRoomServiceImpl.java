package com.mashreq.conference.booking.service.Impl;
import com.mashreq.conference.booking.Repository.BookingRepository;
import com.mashreq.conference.booking.helper.Entity.BookingEntity;
import com.mashreq.conference.booking.helper.properties.ConferenceRoomConfig;
import com.mashreq.conference.booking.helper.properties.MaintenanceTimingsConfig;
import com.mashreq.conference.booking.service.ConferenceRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConferenceRoomServiceImpl implements ConferenceRoomService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private MaintenanceTimingsConfig maintenanceTimingsConfig;
    @Autowired
    private ConferenceRoomConfig conferenceRoomConfig;
    @Override
    public List<String> getAvailableRooms(String date, String startTime, String endTime) {
        // Parse date, startTime, and endTime strings into LocalDate and LocalTime objects
        LocalDate parsedDate = LocalDate.parse(date);
        LocalTime parsedStartTime = LocalTime.parse(startTime);
        LocalTime parsedEndTime = LocalTime.parse(endTime);

        // Fetch all booked rooms for the given date and time range
        List<String> bookedRooms = bookingRepository.findBookedRooms(parsedDate, parsedStartTime, parsedEndTime);

        // Get the list of rooms from the configuration
        List<ConferenceRoomConfig.ConferenceRoom> roomDetailsList = conferenceRoomConfig.getRooms();

        // Filter rooms based on availability and configuration
        List<String> availableRooms = roomDetailsList.stream()
                .filter(roomDetails -> !bookedRooms.contains(roomDetails.getName()))
                .map(ConferenceRoomConfig.ConferenceRoom::getName)
                .collect(Collectors.toList());

        return availableRooms;
    }
    public ConferenceRoomConfig.ConferenceRoom getConferenceRoomByName(String roomName) {
        // Iterate through the configured rooms and find the one with a matching name
        return conferenceRoomConfig.getRooms().stream()
                .filter(room -> room.getName().equalsIgnoreCase(roomName))
                .findFirst()
                .orElse(null);
    }
    @Override
    public String bookConferenceRoom(String date,String roomName, String startTime, String endTime, int numberOfPeople) {
        // Check if the booking time is within maintenance timings
        LocalDate parsedDate = LocalDate.parse(date);
        if (isDuringMaintenance(LocalTime.parse(startTime)) || isDuringMaintenance(LocalTime.parse(endTime))) {
            return "Booking cannot be done during maintenance time.";
        }
        // Find the conference room by name
        ConferenceRoomConfig.ConferenceRoom conferenceRoom = getConferenceRoomByName(roomName);

        // Check if the room exists
        if (conferenceRoom == null) {
            return "Invalid room name.";
        }
        // Check if the room has the capacity for the requested number of people
        if (numberOfPeople <= 0 || numberOfPeople > conferenceRoom.getCapacity()) {
            return "Invalid number of people or room capacity exceeded.";
        }
        // Check if the booking time is in intervals of 15 minutes
        if (!isValidBookingInterval(startTime, endTime)) {
            return "Booking should be done only in intervals of 15 minutes.";
        }
        // Check if the start time is before the end time
        if (!isValidTimeRange(startTime, endTime)) {
            return "Invalid time range.";
        }
        // Check if the room is available for the given time range
        if (!isRoomAvailable(roomName, LocalDate.now(),LocalTime.parse(startTime), LocalTime.parse(endTime))) {
            return "Room is not available for the specified time range.";
        }
        // Save the booking details to the database
        BookingEntity booking = new BookingEntity(parsedDate,roomName, LocalTime.parse(startTime), LocalTime.parse(endTime),numberOfPeople);
        bookingRepository.save(booking);
        return "Booking successful!";
    }
    public boolean isValidTimeRange(String startTime, String endTime) {
        LocalTime startDateTime = LocalTime.parse(startTime);
        LocalTime endDateTime = LocalTime.parse(endTime);
        return startDateTime.isBefore(endDateTime);
    }
    public boolean isValidBookingInterval(String startTime, String endTime) {
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);

        long minutesBetween = java.time.Duration.between(start, end).toMinutes();
        return minutesBetween % 15 == 0;
    }

    private boolean isRoomAvailable(String roomName, LocalDate currentDate, LocalTime startTime, LocalTime endTime) {
        List<BookingEntity> bookingsForRoom = bookingRepository.findByRoomNameAndDateAndEndTimeAfterAndStartTimeBefore(
                roomName, currentDate, startTime, endTime);
        return bookingsForRoom.isEmpty();
    }

    public boolean isDuringMaintenance(LocalTime time) {
        List<MaintenanceTimingsConfig.TimeRange> maintenanceTimings = maintenanceTimingsConfig.getTimings();
        for (MaintenanceTimingsConfig.TimeRange maintenanceTime : maintenanceTimings) {
            LocalTime startTime = LocalTime.parse(maintenanceTime.getStart());
            LocalTime endTime = LocalTime.parse(maintenanceTime.getEnd());

            if (time.isAfter(startTime) && time.isBefore(endTime)) {
                return true;
            }
        }
        return false;
    }
}
