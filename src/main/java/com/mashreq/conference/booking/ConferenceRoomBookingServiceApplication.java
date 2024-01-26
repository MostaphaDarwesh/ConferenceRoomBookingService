package com.mashreq.conference.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.mashreq.conference.booking")
public class ConferenceRoomBookingServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ConferenceRoomBookingServiceApplication.class, args);
	}
}
