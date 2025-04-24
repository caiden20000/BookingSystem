package codes.c2k.BookingMicroservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bookingService")
public class BookingService {
    @Autowired
    BookingRepository repository;
    public void saveBooking(Booking newBooking) {
        repository.save(newBooking);
    }
}
