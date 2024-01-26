package com.mashreq.conference.booking.helper.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app.maintenance")
@Data
public class MaintenanceTimingsConfig {

    private List<TimeRange> timings;

    @Data
    public static class TimeRange {
        private String start;
        private String end;

        public TimeRange(String start, String end) {
            this.start = start;
            this.end = end;
        }
    }
}
