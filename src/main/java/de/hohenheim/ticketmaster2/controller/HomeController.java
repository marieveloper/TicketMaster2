package de.hohenheim.ticketmaster2.controller;

import de.hohenheim.ticketmaster2.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
    @Autowired
    private TicketService ticketService;
    /**
     * Zeigt die Startseite Ihrer Anwendung.
     * @param model enthält alle ModelAttribute.
     * @return home-Seite.
     */
    @GetMapping("/home")
    public String showHome(Model model) {
        model.addAttribute("tickets", ticketService.findAllTickets());
        return "home";
    }

    @GetMapping("/admindashboard")
    public String showAdminDashboard(Model model) {
        model.addAttribute("message", "Und hier sehen Sie ein ModelAttribut");
        return "login";
    }

    @PostMapping("/createticket")
    public String createTicket(Model model){
        model.addAttribute("message", "Und hier sehen Sie ein ModelAttribut");
        return "";
    }

}