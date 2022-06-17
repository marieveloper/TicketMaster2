
package de.hohenheim.ticketmaster2.controller;

import de.hohenheim.ticketmaster2.entity.Notification;
import de.hohenheim.ticketmaster2.entity.Ticket;
import de.hohenheim.ticketmaster2.entity.User;
import de.hohenheim.ticketmaster2.enums.Prioritization;
import de.hohenheim.ticketmaster2.enums.Status;
import de.hohenheim.ticketmaster2.service.NotificationService;
import de.hohenheim.ticketmaster2.service.TicketService;
import de.hohenheim.ticketmaster2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;
    /**
     * Zeigt die Startseite Ihrer Anwendung.
     * @param model enthält alle ModelAttribute.
     * @return home-Seite.
     */
    @GetMapping( "/")
    public String showHome(Model model) {
        if (userService.hasRole("ROLE_ADMIN", userService.getCurrentUser())) {
            User admin = userService.getCurrentUser();
            model.addAttribute("admin", admin);
            model.addAttribute("adminNotifications");
            return "admin";
        }
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        return "user";
    }
    @ModelAttribute("admin")
    public User getAdmin(){return userService.getCurrentUser();}
    @ModelAttribute("tickets")
    public List<Ticket> getTickets() {
        return ticketService.findAllTickets();
    }


    @ModelAttribute("userTickets")
    public List<Ticket> getUserTickets() {
        return ticketService.getAllTicketsByUserId(userService.getCurrentUser().getUserId());}

    @ModelAttribute("adminNotifications")
    public List<Notification> getAdminNotifications(){
        return userService.getCurrentUser().getReceivedNotifications().stream().toList();
    }
    @ModelAttribute("notifications")
    public List<Notification> getNotifications(){
        return notificationService.findAllNotifications();
    }

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
        if (userService.hasRole("ROLE_ADMIN", userService.getCurrentUser())) {
            return "showTicketAdmin";
        }
        return "showTicket";
        }

    @GetMapping("/notifications")
    public String goToNotification( Model model){
        model.addAttribute("adminNotifications");
        return "notifications";
    }

    @GetMapping("/withdrawTicket{ticketId}")
    public String withdrawTicket(@ModelAttribute("ticket") Ticket ticket, @RequestParam Integer ticketId,Model model){
        ticketService.deleteTicket(ticket.getTicketId());
        return "redirect:/user";
    }
    
    @GetMapping("/logout")
    public String logout(){
        return "redirect:/login?logout";
    }

    @GetMapping("/back")
    public String backToUserDashboard(Model model){
        if(userService.hasRole("ROLE_ADMIN", userService.getCurrentUser())) {
            return "redirect:/admin";
        }
        return "redirect:/user";
    }


<<<<<<< HEAD

=======
    @GetMapping("/requestStatus{ticketId}")
    public String requestStatus(@ModelAttribute("ticket") Ticket ticket, @RequestParam Integer ticketId, Model model){
        Notification notificationTest = new Notification();
        model.addAttribute("notifications", notificationTest);
        notificationTest.setTicket(ticketService.getByTicketId(ticketId));
        notificationTest.setText("Get Statusupdate for ticket with id: " + ticketId +"!");
        notificationTest.setSender(notificationTest.getTicket().getUser());
        notificationTest.setReceiver(notificationTest.getTicket().getResponsibleAdmin());
        notificationService.saveNotification(notificationTest);
    return "redirect:/user";
    }

    @GetMapping("/workInProgress")
    public String workInProgress(){
        return "redirect:/workInProgress";
    }

    @GetMapping("/showTicketAdmin{ticketId}")
    public String gotoTicketAdmin(@RequestParam Integer ticketId, Model model){
        Ticket ticket = ticketService.getByTicketId(ticketId);
        model.addAttribute("ticket",ticket);
        return "showTicketAdmin";
    }
>>>>>>> eb0ee1bafd827ee1acc7044a2b9c3e72b069f731

}