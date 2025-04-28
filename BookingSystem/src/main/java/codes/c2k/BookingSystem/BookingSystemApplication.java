package codes.c2k.BookingSystem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.h2.tools.Server;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class BookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingSystemApplication.class, args);
	}

	@Bean(initMethod = "start", destroyMethod = "stop")
	public Server inMemoryH2DatabaseaServer() throws SQLException {
		log.info(">> Starting H2 Server...");
		return Server.createTcpServer(
		"-tcp", "-tcpAllowOthers", "-tcpPort", "60606");
	}

	@Bean
	public CommandLineRunner demo(BookingService service) {
		return (args) -> {
			log.info("Adding example bookings...");

			// Used ChatGPT to generate 12 examples
			
			// Booking 1: Single day event, Employer EMP1, Chef CH1 (1st of 2 for CH1)
			PaymentAgreement agreement1 = new PaymentAgreement();
			agreement1.setSignOnPay(200)
					.setHourlyPay(20)
					.setCompletionPay(400);

			List<BookingTime> schedule1 = new ArrayList<>();
			schedule1.add(BookingTime.sameDay(2025, 5, 1, 8, 0, 16, 0)); // 1-day event

			Booking booking1 = new Booking();
			booking1.setBookingId("B001")
					.setEmployerId("EMP1") // Employer 1
					.setChefId("CH1") // Chef 1 (CH1 gets 2 bookings total)
					.setStatus(BookingStatus.PENDING_CHEF)
					.setDescription("Single day breakfast meeting.")
					.setSchedule(schedule1)
					.setPaymentAgreement(agreement1);

			service.saveBooking(booking1);

			// Booking 2: Two-day event, Employer EMP1, Chef CH1 (2nd of 2 for CH1)
			PaymentAgreement agreement2 = new PaymentAgreement();
			agreement2.setSignOnPay(220)
					.setHourlyPay(22)
					.setCompletionPay(420);

			List<BookingTime> schedule2 = new ArrayList<>();
			schedule2.add(BookingTime.sameDay(2025, 5, 2, 9, 0, 17, 0));
			schedule2.add(BookingTime.sameDay(2025, 5, 3, 9, 0, 17, 0)); // 2-day event

			Booking booking2 = new Booking();
			booking2.setBookingId("B002")
					.setEmployerId("EMP1")
					.setChefId("CH1")
					.setStatus(BookingStatus.PENDING_EMPLOYER)
					.setDescription("Two-day workshop brunch.")
					.setSchedule(schedule2)
					.setPaymentAgreement(agreement2);

			service.saveBooking(booking2);

			// Booking 3: Three-day event, Employer EMP1, Chef CH2 (1st of 4 for CH2)
			PaymentAgreement agreement3 = new PaymentAgreement();
			agreement3.setSignOnPay(240)
					.setHourlyPay(24)
					.setCompletionPay(440);

			List<BookingTime> schedule3 = new ArrayList<>();
			schedule3.add(BookingTime.sameDay(2025, 5, 4, 7, 30, 15, 30));
			schedule3.add(BookingTime.sameDay(2025, 5, 5, 7, 30, 15, 30));
			schedule3.add(BookingTime.sameDay(2025, 5, 6, 7, 30, 15, 30)); // 3-day event

			Booking booking3 = new Booking();
			booking3.setBookingId("B003")
					.setEmployerId("EMP1")
					.setChefId("CH2") // Chef 2 (first booking for CH2)
					.setStatus(BookingStatus.ACCEPTED)
					.setDescription("Three-day weekend brunch event.")
					.setSchedule(schedule3)
					.setPaymentAgreement(agreement3);

			service.saveBooking(booking3);

			// Booking 4: Four-day event, Employer EMP1, Chef CH2 (2nd for CH2)
			PaymentAgreement agreement4 = new PaymentAgreement();
			agreement4.setSignOnPay(260)
					.setHourlyPay(26)
					.setCompletionPay(460);

			List<BookingTime> schedule4 = new ArrayList<>();
			schedule4.add(BookingTime.sameDay(2025, 5, 7, 8, 30, 16, 30));
			schedule4.add(BookingTime.sameDay(2025, 5, 8, 8, 30, 16, 30));
			schedule4.add(BookingTime.sameDay(2025, 5, 9, 8, 30, 16, 30));
			schedule4.add(BookingTime.sameDay(2025, 5, 10, 8, 30, 16, 30)); // 4-day event

			Booking booking4 = new Booking();
			booking4.setBookingId("B004")
					.setEmployerId("EMP1")
					.setChefId("CH2")
					.setStatus(BookingStatus.REJECTED)
					.setDescription("Four-day culinary training session.")
					.setSchedule(schedule4)
					.setPaymentAgreement(agreement4);

			service.saveBooking(booking4);

			// Booking 5: Five-day event, Employer EMP1, Chef CH2 (3rd for CH2)
			PaymentAgreement agreement5 = new PaymentAgreement();
			agreement5.setSignOnPay(280)
					.setHourlyPay(28)
					.setCompletionPay(480);

			List<BookingTime> schedule5 = new ArrayList<>();
			schedule5.add(BookingTime.sameDay(2025, 5, 11, 7, 0, 15, 0));
			schedule5.add(BookingTime.sameDay(2025, 5, 12, 7, 0, 15, 0));
			schedule5.add(BookingTime.sameDay(2025, 5, 13, 7, 0, 15, 0));
			schedule5.add(BookingTime.sameDay(2025, 5, 14, 7, 0, 15, 0));
			schedule5.add(BookingTime.sameDay(2025, 5, 15, 7, 0, 15, 0)); // 5-day event

			Booking booking5 = new Booking();
			booking5.setBookingId("B005")
					.setEmployerId("EMP1") // EMP1 now has 5 bookings
					.setChefId("CH2")
					.setStatus(BookingStatus.CANCELLED)
					.setDescription("Five-day gourmet cooking camp.")
					.setSchedule(schedule5)
					.setPaymentAgreement(agreement5);

			service.saveBooking(booking5);

			// Booking 6: Three-day event (different time), Employer EMP2, Chef CH2 (4th for
			// CH2)
			PaymentAgreement agreement6 = new PaymentAgreement();
			agreement6.setSignOnPay(300)
					.setHourlyPay(30)
					.setCompletionPay(500);

			List<BookingTime> schedule6 = new ArrayList<>();
			schedule6.add(BookingTime.sameDay(2025, 5, 16, 10, 0, 18, 0));
			schedule6.add(BookingTime.sameDay(2025, 5, 17, 10, 0, 18, 0));
			schedule6.add(BookingTime.sameDay(2025, 5, 18, 10, 0, 18, 0)); // 3-day event

			Booking booking6 = new Booking();
			booking6.setBookingId("B006")
					.setEmployerId("EMP2") // Switch to Employer 2
					.setChefId("CH2")
					.setStatus(BookingStatus.COMPLETED)
					.setDescription("Three-day corporate brunch.")
					.setSchedule(schedule6)
					.setPaymentAgreement(agreement6);

			service.saveBooking(booking6);

			// Booking 7: Seven-day event, Employer EMP2, Chef CH3 (1st for CH3)
			PaymentAgreement agreement7 = new PaymentAgreement();
			agreement7.setSignOnPay(320)
					.setHourlyPay(32)
					.setCompletionPay(520);

			List<BookingTime> schedule7 = new ArrayList<>();
			for (int day = 19; day <= 25; day++) {
				schedule7.add(BookingTime.sameDay(2025, 5, day, 8, 0, 16, 0));
			}

			Booking booking7 = new Booking();
			booking7.setBookingId("B007")
					.setEmployerId("EMP2")
					.setChefId("CH3") // Chef 3 gets 6 bookings total
					.setStatus(BookingStatus.PENDING_CHEF)
					.setDescription("A week-long culinary retreat.")
					.setSchedule(schedule7)
					.setPaymentAgreement(agreement7);

			service.saveBooking(booking7);

			// Booking 8: Eight-day event, Employer EMP2, Chef CH3 (2nd for CH3)
			PaymentAgreement agreement8 = new PaymentAgreement();
			agreement8.setSignOnPay(340)
					.setHourlyPay(34)
					.setCompletionPay(540);

			List<BookingTime> schedule8 = new ArrayList<>();
			schedule8.add(BookingTime.sameDay(2025, 5, 26, 9, 0, 17, 0));
			schedule8.add(BookingTime.sameDay(2025, 5, 27, 9, 0, 17, 0));
			schedule8.add(BookingTime.sameDay(2025, 5, 28, 9, 0, 17, 0));
			schedule8.add(BookingTime.sameDay(2025, 5, 29, 9, 0, 17, 0));
			schedule8.add(BookingTime.sameDay(2025, 5, 30, 9, 0, 17, 0));
			schedule8.add(BookingTime.sameDay(2025, 5, 31, 9, 0, 17, 0));
			schedule8.add(BookingTime.sameDay(2025, 6, 1, 9, 0, 17, 0));
			schedule8.add(BookingTime.sameDay(2025, 6, 2, 9, 0, 17, 0)); // 8-day event

			Booking booking8 = new Booking();
			booking8.setBookingId("B008")
					.setEmployerId("EMP2")
					.setChefId("CH3")
					.setStatus(BookingStatus.PENDING_EMPLOYER)
					.setDescription("Eight-day culinary festival.")
					.setSchedule(schedule8)
					.setPaymentAgreement(agreement8);

			service.saveBooking(booking8);

			// Booking 9: Nine-day event, Employer EMP2, Chef CH3 (3rd for CH3)
			PaymentAgreement agreement9 = new PaymentAgreement();
			agreement9.setSignOnPay(360)
					.setHourlyPay(36)
					.setCompletionPay(560);

			List<BookingTime> schedule9 = new ArrayList<>();
			for (int day = 3; day <= 11; day++) {
				schedule9.add(BookingTime.sameDay(2025, 6, day, 7, 0, 15, 0));
			}

			Booking booking9 = new Booking();
			booking9.setBookingId("B009")
					.setEmployerId("EMP2")
					.setChefId("CH3")
					.setStatus(BookingStatus.ACCEPTED)
					.setDescription("Nine-day series of cooking classes.")
					.setSchedule(schedule9)
					.setPaymentAgreement(agreement9);

			service.saveBooking(booking9);

			// Booking 10: Ten-day event, Employer EMP2, Chef CH3 (4th for CH3)
			PaymentAgreement agreement10 = new PaymentAgreement();
			agreement10.setSignOnPay(380)
					.setHourlyPay(38)
					.setCompletionPay(580);

			List<BookingTime> schedule10 = new ArrayList<>();
			for (int day = 12; day <= 21; day++) {
				schedule10.add(BookingTime.sameDay(2025, 6, day, 10, 0, 18, 0));
			}

			Booking booking10 = new Booking();
			booking10.setBookingId("B010")
					.setEmployerId("EMP2")
					.setChefId("CH3")
					.setStatus(BookingStatus.REJECTED)
					.setDescription("Ten-day intensive gourmet training.")
					.setSchedule(schedule10)
					.setPaymentAgreement(agreement10);

			service.saveBooking(booking10);

			// Booking 11: Eleven-day event, Employer EMP2, Chef CH3 (5th for CH3)
			PaymentAgreement agreement11 = new PaymentAgreement();
			agreement11.setSignOnPay(400)
					.setHourlyPay(40)
					.setCompletionPay(600);

			List<BookingTime> schedule11 = new ArrayList<>();
			for (int day = 22; day <= 30; day++) {
				schedule11.add(BookingTime.sameDay(2025, 6, day, 8, 0, 16, 0));
			}
			schedule11.add(BookingTime.sameDay(2025, 7, 1, 8, 0, 16, 0)); // 11-day event

			Booking booking11 = new Booking();
			booking11.setBookingId("B011")
					.setEmployerId("EMP2")
					.setChefId("CH3")
					.setStatus(BookingStatus.CANCELLED)
					.setDescription("Eleven-day culinary masterclass.")
					.setSchedule(schedule11)
					.setPaymentAgreement(agreement11);

			service.saveBooking(booking11);

			// Booking 12: Twelve-day event, Employer EMP2, Chef CH3 (6th for CH3)
			PaymentAgreement agreement12 = new PaymentAgreement();
			agreement12.setSignOnPay(420)
					.setHourlyPay(42)
					.setCompletionPay(620);

			List<BookingTime> schedule12 = new ArrayList<>();
			for (int day = 2; day <= 13; day++) {
				schedule12.add(BookingTime.sameDay(2025, 7, day, 9, 0, 17, 0));
			}

			Booking booking12 = new Booking();
			booking12.setBookingId("B012")
					.setEmployerId("EMP2")
					.setChefId("CH3")
					.setStatus(BookingStatus.COMPLETED)
					.setDescription("Twelve-day extended culinary extravaganza.")
					.setSchedule(schedule12)
					.setPaymentAgreement(agreement12);

			service.saveBooking(booking12);

			log.info("Example bookings added!");
		};
	}
}
