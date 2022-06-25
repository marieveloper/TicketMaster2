
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
     *
     * @param model enthält alle ModelAttribute.
     * @return home-Seite.
     */
    @GetMapping("/")
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
    public User getAdmin() {
        return userService.getCurrentUser();
    }

    @ModelAttribute("tickets")
    public List<Ticket> getTickets() {
        return ticketService.findAllTickets();
    }


    @ModelAttribute("userTickets")
    public List<Ticket> getUserTickets() {
        return ticketService.getAllTicketsByUserId(userService.getCurrentUser().getUserId());
    }

    @ModelAttribute("adminNotifications")
    public List<Notification> getAdminNotifications() {
        return userService.getCurrentUser().getReceivedNotifications().stream().toList();
    }

    @ModelAttribute("notifications")
    public List<Notification> getNotifications() {
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
    public String createTicket(Ticket ticket, Model model) {
        Ticket newTicket = new Ticket();
        model.addAttribute("ticket", newTicket);
        return "createTicket";
    }

    @PostMapping("/saveTicket")
    public String createTicket(@ModelAttribute("ticket") Ticket ticket) {
        ticket.setUser(userService.getCurrentUser());
        ticket.setTitle("title");
        ticket.setCreationTime(Timestamp.from(Instant.now()));
        ticket.setStatus(Status.OPEN);
        ticket.setPrio(Prioritization.HIGH);
        ticket.setResponsibleAdmin(userService.getUserByUsername("admin"));
        ticketService.add(ticket);
        return "redirect:/user";
    }

    @GetMapping("/showTicket{ticketId}")
    public String gotoTicket(@RequestParam Integer ticketId, Model model) {
        Ticket ticket = ticketService.getByTicketId(ticketId);
        model.addAttribute("ticket", ticket);
        if (userService.hasRole("ROLE_ADMIN", userService.getCurrentUser())) {
            return "showTicketAdmin";
        }
        return "showTicket";
    }

    @GetMapping("/notifications")
    public String goToNotification(Model model) {
        model.addAttribute("adminNotifications");
        return "notifications";
    }

    @GetMapping("/withdrawTicket{ticketId}")
    public String withdrawTicket(@ModelAttribute("ticket") Ticket ticket, @RequestParam Integer ticketId, Model model) {
        Notification notificationDelete = new Notification();
        model.addAttribute("notifications", notificationDelete);
        notificationDelete.setText("The ticket with id " + ticketId + " was deleted");
        notificationDelete.setReceiver(ticket.getResponsibleAdmin()); //TODO ist null???
        notificationDelete.setSender(ticket.getUser()); //TODO ist null???
        notificationService.saveNotification(notificationDelete);
        ticket.setRequestTime(Timestamp.from(Instant.now()));

        ticketService.deleteTicket(ticket.getTicketId());
        return "redirect:/user";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }

    @GetMapping("/back")
    public String backToUserDashboard(Model model) {
        if (userService.hasRole("ROLE_ADMIN", userService.getCurrentUser())) {
            return "redirect:/admin";
        }
        return "redirect:/user";
    }

    @GetMapping("/requestStatus{ticketId}")
    public String requestStatus(@ModelAttribute("ticket") Ticket ticket, @RequestParam Integer ticketId, Model model) {
        if (ticketService.canRequestStatus(ticketId)){
            Notification notificationTest = new Notification();
            model.addAttribute("notifications", notificationTest);
            notificationTest.setTicket(ticketService.getByTicketId(ticketId));
            notificationTest.setText("Get Statusupdate for ticket with id: " + ticketId + "!");
            notificationTest.setSender(notificationTest.getTicket().getUser());
            notificationTest.setReceiver(notificationTest.getTicket().getResponsibleAdmin());
            notificationService.saveNotification(notificationTest);
            ticket.setRequestTime(Timestamp.from(Instant.now()));
            return "redirect:/user";
        } System.out.print("Too soon ");
        return gotoTicket(ticketId, model);

    }

    @GetMapping("/workInProgress")
    public String workInProgress() {
        return "redirect:/workInProgress";
    }

    @GetMapping("/showTicketAdmin{ticketId}")
    public String gotoTicketAdmin(@RequestParam Integer ticketId, Model model) {
        Ticket ticket = ticketService.getByTicketId(ticketId);
        model.addAttribute("ticket", ticket);
        return "showTicketAdmin";
    }
}