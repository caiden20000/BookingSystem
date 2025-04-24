package codes.c2k.BookingMicroservice;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface BookingRepository extends CrudRepository<Booking, Long> {

    List<Booking> findByEmployerId(String employerId);
    List<Booking> findByChefId(String chefId);
    Optional<Booking> findById(long id);
    Optional<Booking> findByBookingId(String bookingId);
}
