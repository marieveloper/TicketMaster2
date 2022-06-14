package de.hohenheim.ticketmaster2.entity;

import javax.persistence.*;

@Entity
public class Message {
    @Id
    @GeneratedValue
    private Integer id;

    private String text;
    @ManyToOne
    @JoinColumn(name = "authorId")
    private User author;
    @ManyToOne
    @JoinColumn(name = "receiverId")
    private User receiver;
    @ManyToOne
    @JoinColumn(name = "ticketId")
    private Ticket ticket;

    public Message() {
        //empty constructor for Hibernate
    }

    public Integer getId() {
        return id;
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

    public void setId(Integer id) {
        this.id = id;
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
