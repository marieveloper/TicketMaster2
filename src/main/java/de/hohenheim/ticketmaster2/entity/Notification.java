package de.hohenheim.ticketmaster2.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
public class Notification {
    @Id
    @GeneratedValue
    private Integer id;

    private String text;
    @ManyToOne
    @JsonBackReference
    private User sender;
    @ManyToOne
    @JsonBackReference
    private User receiver;
    @ManyToOne
    @JsonBackReference
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Ticket ticket;

    public Notification() {
        //empty constructor for Hibernate
    }

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public User getSender() {
        return sender;
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

    public void setSender(User author) {
        this.sender = author;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
