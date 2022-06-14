package de.hohenheim.ticketmaster2.controller;

import de.hohenheim.ticketmaster2.service.TicketService;
import de.hohenheim.ticketmaster2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    @GetMapping("/home")
    public String showHome(Model model) {
        if (userService.getCurrentUser().getRoles().equals("ROLE_ADMIN")) {
            return "admindashboard";
        }
        return "userdashboard";
    }

    @GetMapping("/admindashboard")
    public String showAdminDashboard(Model model) {
        model.addAttribute("tickets", ticketService.findAllTickets());
        return "admindashboard";
    }

    @GetMapping("/userdashboard")
    public String showUserDashboard(Model model) {
        model.addAttribute("tickets", ticketService.findAllTickets());
        return "userdashboard";
    }
    @GetMapping("/createTicket")
    public String createTicket(Model model){
        model.addAttribute("message", "Und hier sehen Sie ein ModelAttribut");
        return "createTicket";
    }


}