package codes.c2k.BookingSystem;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class BookingController {

    @Autowired
    BookingService service;

    @Autowired
    NameResolverService nameResolver;

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/list";
    }

    @GetMapping("/view/{bookingId}")
    public String viewBooking(@PathVariable(value = "bookingId") String bookingId, Model model, @CookieValue(value = "userId", defaultValue = "NONE") String userId, @CookieValue(value = "userType", defaultValue = "NONE") String userType) {
        Optional<Booking> result = service.findByBookingId(bookingId);
        if (result.isPresent()) {
            Booking booking = result.get();

            boolean showCancelButton = false;
            boolean showEditButton = false;
            boolean showRejectButton = false;
            boolean showAcceptButton = false;
            if (isValidIdentity(userId, userType)) {
                if (userType.equalsIgnoreCase("employer")) {
                    if (booking.getStatus() == BookingStatus.PENDING_CHEF) {
                        showCancelButton = true;
                    } else if (booking.getStatus() == BookingStatus.PENDING_EMPLOYER) {
                        showAcceptButton = true;
                        showRejectButton = true;
                    } else if (booking.getStatus() == BookingStatus.ACCEPTED) {
                        showCancelButton = true;
                        showEditButton = true;
                    }
                } else {
                    if (booking.getStatus() == BookingStatus.PENDING_CHEF) {
                        showAcceptButton = true;
                        showRejectButton = true;
                    } else if (booking.getStatus() == BookingStatus.PENDING_EMPLOYER) {
                        showCancelButton = true;
                    } else if (booking.getStatus() == BookingStatus.ACCEPTED) {
                        showCancelButton = true;
                    }
                }
            }
            model.addAttribute("booking", booking);
            model.addAttribute("showCancelButton", showCancelButton);
            model.addAttribute("showEditButton", showEditButton);
            model.addAttribute("showRejectButton", showRejectButton);
            model.addAttribute("showAcceptButton", showAcceptButton);
            model.addAttribute("showListButton", !userId.equals("NONE"));
            return "view";
        } else {
            return bookingNotFoundErrorPage(model);
        }
    }

    // Must have ID cookies set to visit.
    @GetMapping("/list")
    public String listBookings(@CookieValue(value = "userId", defaultValue = "NONE") String userId, @CookieValue(value = "userType", defaultValue = "NONE") String userType, Model model, HttpServletResponse response) {
        if (!isValidIdentity(userId, userType)) {
            return missingIdentificationErrorPage(model);
        }

        List<Booking> results = service.findById(userId, userType);

        model.addAttribute("bookingList", results);
        return "list";
    }

    // Edit a booking based on the ID of the booking.
    // If the booking doesn't exist, return the 404 page.
    // Must have ID cookies set to visit.
    @GetMapping("/edit")
    public String editBooking(@CookieValue(value = "userId", defaultValue = "NONE") String userId, @CookieValue(value = "userType", defaultValue = "NONE") String userType, @RequestParam String bookingId, Model model) {
        if (!isValidIdentity(userId, userType)) {
            return missingIdentificationErrorPage(model);
        }
        
        Optional<Booking> result = service.findByBookingId(bookingId);
        if (result.isPresent()) {
            Booking booking = result.get();
            BookingForm bookingForm = BookingForm.fromBooking(booking);
            model.addAttribute("booking", booking);
            model.addAttribute("bookingForm", bookingForm);
            model.addAttribute("postUrl", "/save/" + bookingId);
            model.addAttribute("createMode", false);
            return "edit";
        } else {
            return bookingNotFoundErrorPage(model);
        }
    }

    @PostMapping("/save/{bookingId}")
    public String saveBooking(@CookieValue(value = "userId", defaultValue = "NONE") String userId, @CookieValue(value = "userType", defaultValue = "NONE") String userType, @PathVariable(value = "bookingId") String bookingId, @ModelAttribute("bookingForm") BookingForm bookingForm, Model model) {
        if (!isValidIdentity(userId, userType)) {
            return missingIdentificationErrorPage(model);
        }

        BookingStatus newStatus;
        if (userType.equalsIgnoreCase("employer")) {
            newStatus = BookingStatus.PENDING_CHEF;
        } else {
            newStatus = BookingStatus.PENDING_EMPLOYER;
        }
        
        Optional<Booking> result = service.findByBookingId(bookingId);
        if (result.isPresent()) {
            try {
                Booking booking = result.get();
                bookingForm.applyToBooking(booking);
                booking.setStatus(newStatus);
                service.saveBooking(booking);
                return "redirect:/view/" + bookingId;
            } catch (DateTimeParseException exception) {
                return incorrectFormattingErrorPage(model);
            } 
        } else {
            return bookingNotFoundErrorPage(model);
        }
    }

    // // Create a booking based on the employer and chef IDs
    // // If the employer or chef IDs fail validation, return the 404 page.
    @GetMapping("/create")
    public String createBooking(@CookieValue(value = "userId", defaultValue = "NONE") String userId, @CookieValue(value = "userType", defaultValue = "NONE") String userType, @RequestParam String employerId, @RequestParam String chefId, Model model) {
        if (!isValidIdentity(userId, userType)) {
            return missingIdentificationErrorPage(model);
        }

        BookingForm bookingForm = BookingForm.emptyBookingForm(employerId, chefId);
        model.addAttribute("bookingForm", bookingForm);
        model.addAttribute("postUrl", "/create");
        model.addAttribute("createMode", true);
        model.addAttribute("employerName", nameResolver.getName(employerId, "employer"));
        model.addAttribute("chefName", nameResolver.getName(chefId, "chef"));
        return "edit";
    }

    // Actually allows creation from chefs as well.
    @PostMapping("/create")
    public String saveCreateBooking(@CookieValue(value = "userId", defaultValue = "NONE") String userId, @CookieValue(value = "userType", defaultValue = "NONE") String userType, @ModelAttribute("bookingForm") BookingForm bookingForm, Model model) {
        if (!isValidIdentity(userId, userType)) {
            return missingIdentificationErrorPage(model);
        }

        BookingStatus newStatus;
        if (userType.equalsIgnoreCase("employer")) {
            newStatus = BookingStatus.PENDING_CHEF;
        } else {
            newStatus = BookingStatus.PENDING_EMPLOYER;
        }

        try {
            Booking newBooking = bookingForm.toBooking(newStatus);
            // Must save in order to set auto-generated bookingId
            service.saveBooking(newBooking);
            return "redirect:/view/" + newBooking.getBookingId();
        } catch (DateTimeParseException exception) {
            return incorrectFormattingErrorPage(model);
        }
    }

    @GetMapping("/auth")
    public String authGateway(@RequestParam Map<String, String> allParams, HttpServletResponse response) throws IOException {
        String id = allParams.get("id");
        String idType = allParams.get("idType");
        String page = allParams.get("page");
        if (id == null || idType == null || page == null) {
            response.sendError(400);
        }

        Cookie refIdCookie = new Cookie("userId", id);
        Cookie refTypeCookie = new Cookie("userType", idType);
        refIdCookie.setPath("/");
        refTypeCookie.setPath("/");
        response.addCookie(refIdCookie);
        response.addCookie(refTypeCookie);

        StringBuilder redirectUrl = new StringBuilder("redirect:/");
        redirectUrl.append(page + "?");
        
        // You can iterate over allParams or access individual parameters by name
        allParams.forEach((key, value) -> {
            if (key.equalsIgnoreCase("page") || key.equalsIgnoreCase("id") || key.equalsIgnoreCase("idType")) return;
            redirectUrl.append(key + "=" + value);
            redirectUrl.append("&");
        });
        // Delete trailing ampersand
        redirectUrl.deleteCharAt(redirectUrl.length()-1);
        return redirectUrl.toString();
    }

    @GetMapping("/change-status/{bookingId}")
    public String changeStatus(Model model, @CookieValue(value = "userId", defaultValue = "NONE") String userId, @CookieValue(value = "userType", defaultValue = "NONE") String userType, @PathVariable String bookingId, @RequestParam BookingStatus status) {
        if (!isValidIdentity(userId, userType)) {
            return missingIdentificationErrorPage(model);
        }

        // TODO: Restrict status changes based on identity
        
        Optional<Booking> result = service.findByBookingId(bookingId);
        if (result.isPresent()) {
            Booking booking = result.get();
            booking.setStatus(status);
            service.saveBooking(booking);
            return "redirect:/view/" + booking.getBookingId();
        } else {
            return bookingNotFoundErrorPage(model);
        }
    }




    private boolean isValidIdentity(String userId, String userType) {
        return !userId.equals("NONE") && !userType.equals("NONE") && ( userType.equalsIgnoreCase("employer") || userType.equalsIgnoreCase("chef") );
    }

    private String bookingNotFoundErrorPage(Model model) {
        model.addAttribute("errorTitle", "Booking not Found");
        model.addAttribute("errorDescription",
                "We're sorry, but the URL provided doesn't link to a valid Booking in our database. Perhaps the URL was entered incorrectly?");
        // We're returning "404.html" with all the template portions filled in.
        // This is not returning a 404 status.
        return "404";
    }

    // private String brokenLinkErrorPage(Model model) {
    //     model.addAttribute("errorTitle", "Invalid URL parameters");
    //     model.addAttribute("errorDescription",
    //             "The URL parameters are invalid, so we can't provide proper results. Perhaps the URL was typed incorrectly? If you followed a link here, you should let the link-owner know their link is broken.");
    //     return "404";
    // }

    private String incorrectFormattingErrorPage(Model model) {
        model.addAttribute("errorTitle", "Incorrectly formatted");
        model.addAttribute("errorDescription",
                "The submitted information was incorrectly formatted. This usually happens because the date or times were not in the proper format. Dates should be 'MM/DD/YYYY', and times should be 'HH:MM AA - HH:MM AA'.");
        return "404";
    }

    private String missingIdentificationErrorPage(Model model) {
        model.addAttribute("errorTitle", "Unauthorized");
        model.addAttribute("errorDescription",
                "You must log in to view this page! Log in to your portal first, then navigate to this page from there.");
        return "404";
    }
}
