package de.hohenheim.ticketmaster2.controller;


import de.hohenheim.ticketmaster2.entity.Message;
import de.hohenheim.ticketmaster2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class ChatController {
    @Autowired
    UserService userService;

    @MessageMapping("/hello")
    @SendTo("/topic/chatWebSockets")
    public Message message(Message message) throws Exception {
        Thread.sleep(1000); // simulated delay
        message.setText("text");
        message.setAuthor(userService.getCurrentUser());
        return new Message();
    }
}
