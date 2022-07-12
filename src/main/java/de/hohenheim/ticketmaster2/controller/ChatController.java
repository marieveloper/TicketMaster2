package de.hohenheim.ticketmaster2.controller;


import de.hohenheim.ticketmaster2.entity.Message;
import de.hohenheim.ticketmaster2.entity.Notification;
import de.hohenheim.ticketmaster2.service.MessageService;
import de.hohenheim.ticketmaster2.service.NotificationService;
import de.hohenheim.ticketmaster2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.HtmlUtils;

@Controller
public class ChatController {
    @Autowired
    UserService userService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    MessageService messageService;

   /* @MessageMapping("/hello")
    @SendTo("/topic/chatWebSocket{ticketId}")
    public Message chatMessage(@PathVariable String ticketId, Message message) throws Exception {
        Thread.sleep(1000); // simulated delay
        message.setText("text");
        message.setAuthor(userService.getCurrentUser());
        messageService.saveMessage(message);
        System.out.print(message);
        return message;
   } */
        @MessageMapping("/hello/{ticketId}")
        @SendTo("/topic/chatWebSocket/{ticketId}")
        public Message sendMessage(Message chatMessage,@PathVariable String ticketId) {
        messageService.saveMessage(chatMessage);
            Notification notificationMessage = new Notification();
            notificationMessage.setTicket(chatMessage.getTicket());
            notificationMessage.setRead(false);
            notificationMessage.setSender(chatMessage.getAuthor());
            notificationMessage.setReceiver(chatMessage.getReceiver());
            notificationMessage.setText("There´s a new chat message concerning ticket " + chatMessage.getTicket().getTicketId());
            notificationService.saveNotification(notificationMessage);
            return chatMessage;
        }

}
