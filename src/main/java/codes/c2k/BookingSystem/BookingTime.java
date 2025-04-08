package codes.c2k.BookingSystem;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Embeddable
@Accessors(chain = true)
@Getter @Setter
public class BookingTime {
    Instant startTime;
    Instant endTime;

    public static BookingTime sameDay(int year, int month, int day, int startHour, int startMinute, int endHour, int endMinute) {
        Instant start = ZonedDateTime.of(year, month, day, startHour, startMinute, 0, 0, ZoneId.of("America/New_York")).toInstant();
        Instant end = ZonedDateTime.of(year, month, day, endHour, endMinute, 0, 0, ZoneId.of("America/New_York")).toInstant();
        BookingTime newBookingTime = new BookingTime();
        newBookingTime.setStartTime(start).setEndTime(end);
        return newBookingTime;
    }
}
