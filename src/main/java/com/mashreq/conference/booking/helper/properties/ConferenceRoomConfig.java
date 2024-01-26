package com.mashreq.conference.booking.helper.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app.conference")
@Data
@AllArgsConstructor
public class ConferenceRoomConfig {
    private List<ConferenceRoom> rooms;
    @Data
    public static class ConferenceRoom {
        private String name;
        private int capacity;

        public ConferenceRoom(String name, int capacity) {
            this.name = name;
            this.capacity = capacity;
        }
    }
}

