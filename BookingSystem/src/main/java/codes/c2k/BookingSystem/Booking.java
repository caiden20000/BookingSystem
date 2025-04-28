package codes.c2k.BookingSystem;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Entity
@Setter
@Getter
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) 
    // GenerationType.SEQUENCE required for bookingID setting.
    private long id;

    // If you want to get a generated bookingId, you must
    // save the booking to the data store BEFORE you getBookingId().
    // This setting is facilitated by the @PrePersist method below.
    private String bookingId = "TEMP";

    private String employerId;
    private String chefId;

    private String employerName;
    private String chefName;
    
    @Setter // ONLY use setter when initializing!!
    @ElementCollection
    @CollectionTable(name = "booking_schedule_times", joinColumns = @JoinColumn(name = "entity_id"))
    // Modify the list by modifying the returned List object.
    private List<BookingTime> schedule;

    @Embedded
    private PaymentAgreement paymentAgreement;
    
    private BookingStatus status;
    private String description;

    protected Booking() {}

    public String getStatusCssColorClass() {
        if (status == BookingStatus.PENDING_CHEF) return "bg-blue";
        if (status == BookingStatus.PENDING_EMPLOYER) return "bg-yellow";
        if (status == BookingStatus.ACCEPTED) return "bg-green";
        if (status == BookingStatus.REJECTED) return "bg-red";
        if (status == BookingStatus.CANCELLED) return "bg-red";
        if (status == BookingStatus.COMPLETED) return "bg-gray";
        return "bg-gray";
    }

    // Sets the bookingId to the generated id value BEFORE storing.
    @PrePersist
    public void onPrePersist() {
        this.bookingId = Long.toString(id);
    }

}
