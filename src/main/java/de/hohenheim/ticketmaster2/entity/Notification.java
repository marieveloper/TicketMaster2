package de.hohenheim.ticketmaster2.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Notification {
    @Id
    @GeneratedValue
    private Integer id;

    private String text;
    @ManyToOne

    private User sender;
    @ManyToOne

    private User receiver;
    @ManyToOne

    @OnDelete(action= OnDeleteAction.CASCADE)
    private Ticket ticket;



    private Boolean read;



    public Notification() {
        //empty constructor for Hibernate
    }
    public Notification(String text, User sender, User receiver, Ticket ticket) {
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;
        this.ticket = ticket;
        this.read = false;
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
    public Boolean isRead() {
        return read;
    }
    public void setRead(Boolean read) {
        this.read = read;
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
