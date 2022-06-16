
package de.hohenheim.ticketmaster2.controller;

import de.hohenheim.ticketmaster2.entity.Ticket;
import de.hohenheim.ticketmaster2.enums.Prioritization;
import de.hohenheim.ticketmaster2.enums.Status;
import de.hohenheim.ticketmaster2.service.TicketService;
import de.hohenheim.ticketmaster2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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


    @ModelAttribute("userTickets")
    public List<Ticket> getUserTickets() {
        return ticketService.getAllTicketsByUserId(userService.getCurrentUser().getUserId());}

    @GetMapping("/admin")
    public String showAdminDashboard(Model model) {
       
        model.addAttribute("tickets"); 

        return "admin";
    }

    @GetMapping("/user")
    public String showUserDashboard(Model model) {
        model.addAttribute("userTickets");
        return "user";
    }
    @GetMapping("/createTicket")
    public String createTicket(Ticket ticket,Model model){
        Ticket newTicket = new Ticket();
        model.addAttribute("ticket", newTicket);
        return "createTicket";
    }

    @PostMapping("/saveTicket")
    public String createTicket(@ModelAttribute("ticket") Ticket ticket){
        ticket.setUser(userService.getCurrentUser());
        ticket.setTitle("title");
        ticket.setCreationTime(Timestamp.from(Instant.now().minus(12, ChronoUnit.HOURS)));
        ticket.setStatus(Status.OPEN);
        ticket.setPrio(Prioritization.HIGH);
        ticket.setResponsibleAdmin(userService.getUserByUsername("admin"));
        ticketService.add(ticket);

        return "redirect:/user";
    }

    @GetMapping("/showTicket{ticketId}")
    public String gotoTicket(@RequestParam Integer ticketId, Model model){
        Ticket ticket = ticketService.getByTicketId(ticketId);
        model.addAttribute("ticket",ticket);
        return "showTicket";
        }

    @GetMapping("/withdrawTicket{ticketId}")
    public String withdrawTicket(@ModelAttribute("ticket") Ticket ticket, @RequestParam Integer ticketId,Model model){
        ticketService.deleteTicket(ticket.getTicketId());
        model.addAttribute("userTickets");
        return "user";
    }
    
    @GetMapping("/logout")
    public String logout(){
        return "redirect:/login?logout";
    }

    @GetMapping("/back")
    public String backToUserDashboard(Model model){
        return "redirect:/user";
    }


}