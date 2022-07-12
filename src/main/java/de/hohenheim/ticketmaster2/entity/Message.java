package de.hohenheim.ticketmaster2.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.context.annotation.Bean;

import javax.persistence.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Message {
    @Id
    @GeneratedValue
    private Integer messageId;

    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    private User author;

    @ManyToOne(fetch = FetchType.EAGER)
    private User receiver;

    @ManyToOne(fetch = FetchType.EAGER)
    private Ticket ticket;

    public Message(String text, User author, User receiver, Ticket ticket) {
        this.text = text;
        this.author = author;
        this.receiver = receiver;
        this.ticket = ticket;
    }

    public Message() {

    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }
    public String getText() {
        return text;
    }

    public User getAuthor() {
        return author;
    }

    public User getReceiver() {
        return receiver;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

}
