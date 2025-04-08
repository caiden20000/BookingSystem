package codes.c2k.BookingSystem;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Embeddable
@Accessors(chain = true)
@Getter @Setter
public class PaymentAgreement {
    float signOnPay;
    float hourlyPay;
    float completionPay;
}
