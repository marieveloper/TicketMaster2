package de.hohenheim.ticketmaster2.controller;

import de.hohenheim.ticketmaster2.entity.Ticket;
import de.hohenheim.ticketmaster2.service.TicketService;
import de.hohenheim.ticketmaster2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;
    /**
     * Zeigt die Startseite Ihrer Anwendung.
     * @param model enthält alle ModelAttribute.
     * @return home-Seite.
     */
    @GetMapping( "/")
    public String showHome(Model model) {
        System.out.print(userService.getCurrentUser().getUsername());
        if (userService.hasRole("ROLE_ADMIN", userService.getCurrentUser())) {
            System.out.print("Hallo");
            return "admin";
        }
        return "user";
    }


    @GetMapping("/admin")
    public String showAdminDashboard(Model model) {
        model.addAttribute("tickets", ticketService.findAllTickets());
        return "admin";
    }

    @GetMapping("/user")
    public String showUserDashboard(Model model) {
        model.addAttribute("tickets", ticketService.findAllTickets());
        return "user";
    }
    @GetMapping("/createTicket")
    public String createTicket(Model model){
        model.addAttribute("ticket", new Ticket());
        return "createTicket";
    }

    @PostMapping("/saveTicket")
    public String createTicket(@ModelAttribute Ticket ticket){
        ticketService.saveTicket(ticket);
        return "redirect:/userdashboard";
    }

    @GetMapping("/back")
    public String backToUserDashboard(Model model){
        model.addAttribute("tickets", ticketService.findAllTickets());
        return "user";
    }


}