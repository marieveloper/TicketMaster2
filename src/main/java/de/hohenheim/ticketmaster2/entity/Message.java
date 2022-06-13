package de.hohenheim.ticketmaster2.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Message {
    @Id
    @GeneratedValue
    private Integer id;

    private String text;
    private User author;
    private User receiver;
    private Ticket ticket;

    public Message() {
        //empty constructor for Hibernate
    }
}
