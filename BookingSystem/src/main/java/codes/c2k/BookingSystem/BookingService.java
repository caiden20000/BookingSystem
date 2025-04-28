package codes.c2k.BookingSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    
    private final NameResolverService nameResolver;
    private final BookingRepository repository;

    @Autowired
    public BookingService(NameResolverService nameResolver, BookingRepository repository) {
        this.nameResolver = nameResolver;
        this.repository = repository;
    }

    public void saveBooking(Booking booking) {
        // Retrieve the names from external API if not yet set.
        if (booking.getEmployerName() == null) {
            booking.setEmployerName(nameResolver.getName(booking.getEmployerId(), "employer"));
        }

        if (booking.getChefName() == null) {
            booking.setChefName(nameResolver.getName(booking.getChefId(), "chef"));
        }
        repository.save(booking);
    }

    public Optional<Booking> findByBookingId(String bookingId) {
        return repository.findByBookingId(bookingId);
    }

    public List<Booking> findById(String id, String idType) {
        if (idType.equalsIgnoreCase("employer")) {
            return repository.findByEmployerId(id);
        } else if (idType.equalsIgnoreCase("chef")) {
            return repository.findByChefId(id);
        } else {
            return new ArrayList<Booking>();
        }
    }

}
