package codes.c2k.BookingSystem;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter @Setter
public class BookingTimeForm {
    String date;
    String startTime;
    String endTime;

    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a");

    public BookingTimeForm() {}

    public static BookingTimeForm fromBookingTime(BookingTime time) {
        LocalDateTime startTime = LocalDateTime.ofInstant(time.startTime, BookingConfig.timezoneId);
        LocalDateTime endTime = LocalDateTime.ofInstant(time.endTime, BookingConfig.timezoneId);

        BookingTimeForm newForm = new BookingTimeForm();
        newForm.setDate(startTime.format(dateFormat));
        newForm.setStartTime(startTime.format(timeFormat));
        newForm.setEndTime(endTime.format(timeFormat));

        return newForm;
    }

    public BookingTime toBookingTime() throws DateTimeParseException {
        LocalDate newLocalDate = LocalDate.parse(date, dateFormat);
        LocalTime newLocalStartTime = LocalTime.parse(startTime, timeFormat);
        LocalTime newLocalEndTime = LocalTime.parse(endTime, timeFormat);

        Instant newStartTime = LocalDateTime.of(newLocalDate, newLocalStartTime).atZone(BookingConfig.timezoneId).toInstant();
        Instant newEndTime = LocalDateTime.of(newLocalDate, newLocalEndTime).atZone(BookingConfig.timezoneId).toInstant();

        BookingTime newBookingTime = new BookingTime();
        newBookingTime.setStartTime(newStartTime)
        .setEndTime(newEndTime);

        return newBookingTime;
    }
}
