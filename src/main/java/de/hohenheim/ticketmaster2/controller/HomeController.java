
package de.hohenheim.ticketmaster2.controller;

import de.hohenheim.ticketmaster2.entity.Message;
import de.hohenheim.ticketmaster2.entity.Notification;
import de.hohenheim.ticketmaster2.entity.Ticket;
import de.hohenheim.ticketmaster2.entity.User;
import de.hohenheim.ticketmaster2.enums.Status;
import de.hohenheim.ticketmaster2.service.MessageService;
import de.hohenheim.ticketmaster2.service.NotificationService;
import de.hohenheim.ticketmaster2.service.TicketService;
import de.hohenheim.ticketmaster2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

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
        } else {
            User user = userService.getCurrentUser();
            model.addAttribute("user", user);
            return "user";
        }
    }

    @ModelAttribute("user")
    public User getUser() {
        return userService.getCurrentUser();
    }

    @ModelAttribute("admin")
    public User getAdmin() {
        return userService.getCurrentUser();
    }

    @ModelAttribute("tickets")
    public List<Ticket> getTickets() {
        return ticketService.findAllTickets();
    }

    @ModelAttribute("admins")
    public List<User> getAllAdmins() {
        List<User> admins = new LinkedList<>();
        List<User> allUsers = userService.findAllUsers();
        for (User user : allUsers) {
            if (userService.hasRole("ROLE_ADMIN", user)) {
                admins.add(user);
            }
        }
        return admins;
    }

    @ModelAttribute("userTickets")
    public List<Ticket> getUserTickets() {
        return ticketService.getAllTicketsByUserId(userService.getCurrentUser().getUserId());
    }

    @ModelAttribute("adminTickets")
    public List<Ticket> getAdminTickets() {
        return ticketService.getAllTicketsByAdminId(userService.getCurrentUser().getUserId());
    }

    @ModelAttribute("adminNotifications")
    public List<Notification> getCurrentUserNotifications() {
        return notificationService.findAllNotifications().stream()
                .filter(n -> n.getReceiver().getUserId() == userService.getCurrentUser().getUserId()).toList();
    }

    @ModelAttribute("notifications")
    public List<Notification> getNotifications() {
        return notificationService.findAllNotifications();
    }

    @ModelAttribute("messages")
    public List<Message> getMessages() {
        return messageService.findAllMessages();
    }

    @ModelAttribute("unreadAdminNotifications")
    public List<Notification> getUnreadAdminNotifications(){
        List<Notification> notifications = notificationService.findAllNotifications().stream()
                .filter(n -> n.getReceiver().getUserId() == userService.getCurrentUser().getUserId()).toList();
        return notificationService.findAllUnreadNotifications(notifications);
    }



    //Mappings----------------------------------------------------------------------------------------------------------
    @GetMapping("/admin")
    public String showAdminDashboard(Model model, String keyword) {
        model.addAttribute("tickets", ticketService.findAllTickets());
        return "admin";
    }

    @GetMapping("/user")
    public String showUserDashboard(Model model, String keyword) {
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
        ticket.setCreationTime(Timestamp.from(Instant.now()));
        ticket.setStatus(Status.OPEN);
        ticket.setPrioAuto();
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
        notificationDelete.setReceiver(ticketService.getByTicketId(ticketId).getResponsibleAdmin());
        notificationDelete.setSender(ticketService.getByTicketId(ticketId).getUser());
        notificationDelete.setRead(false);
        notificationService.saveNotification(notificationDelete);
        ticket.setRequestTime(Timestamp.from(Instant.now()));
        ticketService.deleteTicket(ticketId);
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
    public String requestStatus( @RequestParam Integer ticketId, Model model) {
        if (ticketService.canRequestStatus(ticketId)) {
            Notification notificationRequest = new Notification();
            Ticket ticket = ticketService.getByTicketId(ticketId);
            model.addAttribute("notifications", notificationRequest);
            notificationRequest.setTicket(ticketService.getByTicketId(ticketId));
            notificationRequest.setText("Get Statusupdate for ticket with id: " + ticketId + "!");
            notificationRequest.setSender(notificationRequest.getTicket().getUser());
            notificationRequest.setReceiver(notificationRequest.getTicket().getResponsibleAdmin());
            notificationRequest.setRead(false);
            notificationService.saveNotification(notificationRequest);
            ticket.setRequestTime(Timestamp.from(Instant.now()));
            ticketService.saveTicket(ticket);
            return "redirect:/user";
        }
        System.out.print("Too soon ");
        return gotoTicket(ticketId, model);
    }

    @GetMapping("/workInProgress")
    public String workInProgress() {
        return "redirect:/workInProgress";
    }

    @GetMapping("/showTicketAdmin{ticketId}")
    public String gotoTicketAdmin(@RequestParam Integer ticketId, Model model) {
        Ticket ticket = ticketService.getByTicketId(ticketId);
        List messageList = ticketService.findAllTickets();
        model.addAttribute("ticket", ticket);
        return "showTicketAdmin";
    }


    @GetMapping("/editTicket{ticketId}")
    public String editTicket(@RequestParam Integer ticketId, Model model) {
        Ticket ticket = ticketService.getByTicketId(ticketId);
        model.addAttribute("admins");
        model.addAttribute("ticket", ticket);
        return "editTicket";
    }

    @PostMapping("/saveEditedTicket{ticketId}")
    public String editTicket(@ModelAttribute("ticket") Ticket ticket, @RequestParam Integer ticketId, Model model) {
        Ticket oldTicket = ticketService.getByTicketId(ticketId);
        Ticket newTicket = ticket;
        if(newTicket.getStatus()!= oldTicket.getStatus()){
            Notification notificationStatus = new Notification();
            notificationStatus.setTicket(oldTicket);
            notificationStatus.setRead(false);
            notificationStatus.setReceiver(oldTicket.getUser());
            notificationStatus.setSender(oldTicket.getResponsibleAdmin());
            notificationStatus.setText("The status of your Ticket (ID: " + ticketId + ") has changed to "+ newTicket.getStatus());
            notificationService.saveNotification(notificationStatus);
        }
        oldTicket.setResponsibleAdmin(newTicket.getResponsibleAdmin());
        oldTicket.setCategorization(newTicket.getCategorization());
        oldTicket.setPrio(newTicket.getPrio());
        oldTicket.setStatus(newTicket.getStatus());
        ticketService.saveTicket(oldTicket);
        return "redirect:/admin";
    }

    @GetMapping("/chatWebSockets{ticketId}")
    public String chatWebSockets(@RequestParam Integer ticketId,  Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("ticket", ticketService.getByTicketId(ticketId));
        if (userService.hasRole("ROLE_ADMIN", userService.getCurrentUser())) {
            model.addAttribute("receiver", ticketService.getByTicketId(ticketId).getUser());
        }else{
            model.addAttribute("receiver", ticketService.getByTicketId(ticketId).getResponsibleAdmin());
        }

        Message message = new Message("bla", userService.getCurrentUser(), userService.getCurrentUser(), ticketService.getByTicketId(ticketId));
        model.addAttribute("message", message);
        return "chatWebSockets";
    }

    @PostMapping("/notificationRead{id}")
    public String notificationRead(@RequestParam Integer id, Model model) {
        Notification notification = notificationService.getNotificationById(id);
        notification.setRead(true);
        notificationService.saveNotification(notification);
        return "redirect:/notifications";
    }
}