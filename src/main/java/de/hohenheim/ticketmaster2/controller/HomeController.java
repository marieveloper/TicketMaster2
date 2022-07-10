
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public List<Notification> getAdminNotifications() {
        return userService.getCurrentUser().getReceivedNotifications().stream().toList();
    }

    @ModelAttribute("notifications")
    public List<Notification> getNotifications() {
        return notificationService.findAllNotifications();
    }

    @ModelAttribute("messages")
    public List<Message> getMessages() {
        return messageService.findAllMessages();
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
        ticketService.getByTicketId(ticketId).getUser().getSentNotifications().add(notificationDelete);
        ticketService.getByTicketId(ticketId).getResponsibleAdmin().getReceivedNotifications().add(notificationDelete);
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
        if (ticketService.canRequestStatus(ticketId)) {
            Notification notificationTest = new Notification();
            model.addAttribute("notifications", notificationTest);
            notificationTest.setTicket(ticketService.getByTicketId(ticketId));
            notificationTest.setText("Get Statusupdate for ticket with id: " + ticketId + "!");
            notificationTest.setSender(notificationTest.getTicket().getUser());
            notificationTest.setReceiver(notificationTest.getTicket().getResponsibleAdmin());
            notificationTest.setRead(false);
            notificationTest.getTicket().getUser().getSentNotifications().add(notificationTest);
            notificationTest.getTicket().getResponsibleAdmin().getReceivedNotifications().add(notificationTest);
            notificationService.saveNotification(notificationTest);
            ticket.setRequestTime(Timestamp.from(Instant.now()));
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




    @GetMapping("/chat{ticketId}")
    public String sendMessage(@RequestParam Integer ticketId,Model model){
        Ticket ticket = ticketService.getByTicketId(ticketId);
        model.addAttribute("ticket", ticket);
        Message message = new Message();
        model.addAttribute("messages", messageService.findAllMessagesByTicket(ticketId));
        message.setTicket(ticketService.getByTicketId(ticketId));
        message.setAuthor(ticketService.getByTicketId(ticketId).getUser());
        message.setReceiver(ticketService.getByTicketId(ticketId).getResponsibleAdmin());
        message.setText("text");
        messageService.saveMessage(message);
        return "chat";
    }




    @GetMapping("/createMessage{ticketId}")
    public String createMessage(@RequestParam Integer ticketId,Message message, Model model) {
        Message newMessage = new Message();
        model.addAttribute("message", newMessage);
        message.setTicket(ticketService.getByTicketId(ticketId));
        message.setAuthor(ticketService.getByTicketId(ticketId).getUser());
        message.setReceiver(ticketService.getByTicketId(ticketId).getResponsibleAdmin());
        message.setText("text");
        messageService.saveMessage(message);
        return "createMessage";
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
        oldTicket.setResponsibleAdmin(newTicket.getResponsibleAdmin());
        oldTicket.setCategorization(newTicket.getCategorization());
        oldTicket.setPrio(newTicket.getPrio());
        oldTicket.setStatus(newTicket.getStatus());
        ticketService.saveTicket(oldTicket);
        return "redirect:/admin";
    }

    @GetMapping("/chatWebSockets")
    public String chatWebSockets(Model model) {
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