package codes.c2k.BookingMicroservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
public class BookingServiceController {

    @Autowired
    BookingRepository repository;

    @GetMapping("/api/bookings")
    List<Booking> getBookings(@RequestParam String id, @RequestParam String idType) {
        log.info("Got api request at /api/bookings: id= " + id + ", idType= " + idType);
        if (idType.equalsIgnoreCase("employer")) {
            return repository.findByEmployerId(id);
        } else if (idType.equalsIgnoreCase("chef")) {
            return repository.findByChefId(id);
        } else {
            return new ArrayList<>();
        }
    }

    @GetMapping("/api/booking")
    Optional<Booking> getBooking(@RequestParam String bookingId) {
        log.info("Got api request at /api/booking: bookingId= " + bookingId);
        return repository.findByBookingId(bookingId);
    }
    
}

/* This is what the above is designed after.
 * Some changes were made in retrospect.

getBookingsByEmployer(employerID: String): Booking[]
getBookingsByChef(chefID: String): Booking[]
Endpoint:
    GET /api/bookings
Required:
    employerID: String
    OR
    chefID: String
Description:
    Takes either an employer ID or a chef ID. Returns any bookings related to that ID such as accepted, rejected, and pending bookings. If the ID does not match anything in the database, it will return an empty list.
getBooking(bookingID: String): Booking
Endpoint:
    GET /api/booking
Required:
    bookingID: String
Description:
    Returns the booking associated with the given ID. If the ID does not match anything, returns an HTTP error.
*/