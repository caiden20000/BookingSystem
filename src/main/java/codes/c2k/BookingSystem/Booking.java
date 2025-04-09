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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Entity
@Getter
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Setter
    private String bookingId = ((Long) id).toString();

    @Setter
    private String employerId;
    @Setter
    private String chefId;
    
    @Setter // ONLY use setter when initializing!!
    @ElementCollection
    @CollectionTable(name = "booking_schedule_times", joinColumns = @JoinColumn(name = "entity_id"))
    // Modify the list by modifying the returned List object.
    private List<BookingTime> schedule;

    @Setter @Embedded
    private PaymentAgreement paymentAgreement;
    
    @Setter
    private BookingStatus status;
    @Setter
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
    
    
}
