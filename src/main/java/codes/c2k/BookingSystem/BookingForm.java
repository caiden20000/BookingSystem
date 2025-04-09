package codes.c2k.BookingSystem;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter @Setter
public class BookingForm {
    private String bookingId;

    private String employerId;
    private String chefId;

    private float signOnPay;
    private float hourlyPay;
    private float completionPay;

    private List<BookingTimeForm> schedule = new ArrayList<>();

    public BookingForm() {}

    public static BookingForm fromBooking(Booking booking) {
        List<BookingTimeForm> newSchedule = new ArrayList<>();
        for (BookingTime time : booking.getSchedule()) {
            newSchedule.add(BookingTimeForm.fromBookingTime(time));
        }

        BookingForm newForm = new BookingForm();
        newForm.setBookingId(booking.getBookingId())
        .setChefId(booking.getChefId())
        .setEmployerId(booking.getEmployerId())
        .setSignOnPay(booking.getPaymentAgreement().getSignOnPay())
        .setHourlyPay(booking.getPaymentAgreement().getHourlyPay())
        .setCompletionPay(booking.getPaymentAgreement().getCompletionPay())
        .setSchedule(newSchedule);
        return newForm;
    }

    public Booking applyToBooking(Booking booking) throws DateTimeParseException {
        booking.getPaymentAgreement().setSignOnPay(signOnPay).setHourlyPay(hourlyPay).setCompletionPay(completionPay);
        booking.getSchedule().clear();
        for (BookingTimeForm timeForm : schedule) {
            booking.getSchedule().add(timeForm.toBookingTime());
        }

        return booking;
    }

    public Booking toBooking(BookingStatus status) throws DateTimeParseException {
        Booking booking = new Booking();
        PaymentAgreement agreement = new PaymentAgreement();
        agreement.setSignOnPay(signOnPay).setHourlyPay(hourlyPay).setCompletionPay(completionPay);
        List<BookingTime> newSchedule = new ArrayList<>();
        for (BookingTimeForm timeForm : schedule) {
            newSchedule.add(timeForm.toBookingTime());
        }

        booking.setPaymentAgreement(agreement)
        .setChefId(chefId)
        .setEmployerId(employerId)
        .setStatus(status);

        return booking;
    }

    public static BookingForm emptyBookingForm(String employerId, String chefId) {
        BookingForm form = new BookingForm();
        form.setEmployerId(employerId)
        .setChefId(chefId);
        form.getSchedule().add(new BookingTimeForm());
        return form;
    }
}
