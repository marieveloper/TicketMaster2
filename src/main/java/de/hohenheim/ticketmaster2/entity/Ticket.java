package de.hohenheim.ticketmaster2.entity;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

@Entity
public class Ticket {
    @Id
    @GeneratedValue
    private Integer number;

    private String categorization;
    @Basic
    private ArrayList<T> messages;

    @ManyToOne
    @JoinColumn(name = "adminId")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Ticket() {
        // empty constructor for Hibernate
    }
}
