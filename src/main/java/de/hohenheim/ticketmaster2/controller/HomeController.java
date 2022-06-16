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
import java.util.List;

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
        if (userService.hasRole("ROLE_ADMIN", userService.getCurrentUser())) {
            return "admin";
        }
        return "user";
    }

    @ModelAttribute("tickets")
    public List<Ticket> getTickets() {
        return ticketService.findAllTickets();
    }

    @GetMapping("/admin")
    public String showAdminDashboard(Model model) {
       
        model.addAttribute("tickets"); 
       // model.addAttribute("tickets", ticketService.findAllTickets());
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
        return "redirect:/user";
    }


    @GetMapping("/showTicket")
    public String gotoTicket(@ModelAttribute Ticket ticket){
        ticketService.showTicket(ticket);
        return "redirect:/showTicket";
    }


}