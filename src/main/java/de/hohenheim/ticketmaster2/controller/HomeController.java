
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
    public String createTicket(Model model){
        model.addAttribute("ticket", new Ticket());
        return "createTicket";
    }

    @PostMapping("/saveTicket")
    public String createTicket(@ModelAttribute Ticket ticket){
        ticketService.saveTicket(ticket);
        return "redirect:/user";
    }

    @GetMapping("/showTicket{ticketId}")
    public String gotoTicket(@RequestParam Integer ticketId, Model model){
        Ticket ticket = ticketService.getByTicketId(ticketId);
        model.addAttribute("ticket",ticket);
        return "showTicket";
        }


    @GetMapping("/logout")
    public String logout(){
        return "redirect:/login?logout";
    }

    @GetMapping("/back")
    public String backToUserDashboard(Model model){
        model.addAttribute("tickets", ticketService.findAllTickets());
        return "user";
    }




}