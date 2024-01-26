# ConferenceRoomBookingService

**ConferenceRoomService.json -- POSTMAN Collection For the APIs**

**Technology Stack**
1. Java 17 
2. Spring Boot 3
3. Maven
4. In Memory Database H2

**conference room booking REST API for your company’s internal use.**

As of now, there are 4 conference rooms available in the building and all have
different maximum capacities.
Conference Room Details
Conference Name Capacity
Amaze 3 Person
Beauty 7 Person
Inspire 12 Person
Strive 20 Person

Every conference room require some cleaning and maintenance, so every day at
some fixed time it would be serviced and hence cannot be used.
Maintenance Timings for Rooms
9:00 - 9:15
13:00 - 13:15
17:00 - 17: 15


**Rules to follow**


1. Booking can be done only for the current date, so if the current date is 12 JUN
2020 no booking for 13 JUN 2020 can be done.
2. Booking can be done only in intervals of 15 mins, 2:00 - 2:15 or 2:00 - 2:30 or
2:00 - 3:00
3. Booking happens on First Come First Serve.
4. Booking should be optimal, i.e If someone books a room for 5 people then the
ideal room for this booking will be Beauty if not then move to Inspire.
5. Booking cannot be done during maintenance time, if a booking overlaps
maintenance a message should be displayed stating the same.
6. The number of people allowed for booking should be greater than 1 and less
than or equal to the maximum capacity.


**You can safely assume the following points for any input to be always true.**

1. Booking time will always be given by the user in intervals of 15 minutes.
2. Booking time will always be in a 24-hour format
3. Start Time will always be lesser than End Time.

**Use Cases**

1. As a user, I should be able to book a conference meeting room by giving the
time and number of people attending.
2. As a user, I should be able to see meeting rooms available by giving the time
range.


ConferenceRoomService
