package codes.c2k.BookingSystem;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookingController {

    @Autowired
    BookingService service;

    @Autowired
    BookingRepository repository;

    // "/view" is the request URL, which is mapped to the root url, for example: www.booking.com/view
    @GetMapping("/view/{bookingId}") // vvvvvvv This variable is from the path we specified in GetMapping
    public String viewBooking(@PathVariable(value = "bookingId") String bookingId, Model model) {
        Optional<Booking> result = repository.findByBookingId(bookingId);
        if (result.isPresent()) {
            // Here, we're adding a variable for the thymeleaf template to use. ${booking}
            // will refer to result.get()'s output.
            model.addAttribute("booking", result.get());
            // Returning a string from a GetMapping function is returning the templated HTML
            // with that name.
            // So here, we're returning view.html and filling it in with everything passed
            // into the model via addAttribute()
            return "view";
        } else {
            // In this branch, we're going to return an error, which is a different template.
            // This template wants errorTitle and errorDescription to be specified.
            model.addAttribute("errorTitle", "Booking not Found");
            model.addAttribute("errorDescription",
                    "We're sorry, but the URL provided doesn't link to a valid Booking in our database. Perhaps the URL was entered incorrectly?");
            // We're returning "404.html" with all the template portions filled in.
            // This is not returning a 404 status.
            return "404";
        }
    }

    @GetMapping("/list")
    public String viewBooking(@RequestParam String id, @RequestParam String idType, Model model) {
        if (idType.equalsIgnoreCase("employer")) {
            List<Booking> results = repository.findByEmployerId(id);
            model.addAttribute("bookingList", results);
            return "list";
        } else if (idType.equalsIgnoreCase("chef")) {
            List<Booking> results = repository.findByChefId(id);
            model.addAttribute("bookingList", results);
            return "list";
        } else {
            model.addAttribute("errorTitle", "Invalid parameters");
            model.addAttribute("errorDescription",
                    "The URL parameters are invalid, so we can't provide proper results. Perhaps the URL was typed incorrectly? If you followed a link here, you should let the link-owner know their link is broken.");
            return "404";
        }
    }

    // Edit a booking based on the ID of the booking.
    // If the booking doesn't exist, return the 404 page.
    @GetMapping("/edit")
    public String editBooking(@RequestParam String bookingId, Model model) {
        Optional<Booking> result = repository.findByBookingId(bookingId);
        if (result.isPresent()) {
            model.addAttribute("booking", result.get());
            return "edit";
        } else {
            model.addAttribute("errorTitle", "Booking not Found");
            model.addAttribute("errorDescription",
                    "We're sorry, but the URL provided doesn't link to a valid Booking in our database. Perhaps the URL was entered incorrectly?");
            return "404";
        }
    }

    // // Create a booking based on the employer and chef IDs
    // // If the employer or chef IDs fail validation, return the 404 page.
    // @GetMapping("/create")
    // public String createBooking(@RequestParam String employerId, @RequestParam String chefId, Model model) {

    // }

    // @PostMapping("/save")
    // public String saveBooking(@ModelAttribute("booking") Booking booking, Model model) {

    // }
}
