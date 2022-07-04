package de.hohenheim.ticketmaster2.service;


import de.hohenheim.ticketmaster2.entity.Message;
import de.hohenheim.ticketmaster2.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> findAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> findAllMessagesByTicket(Integer ticketId){
        return messageRepository.findAll().stream().filter(n ->n.getTicket().getTicketId()==ticketId).toList();
    }
}
