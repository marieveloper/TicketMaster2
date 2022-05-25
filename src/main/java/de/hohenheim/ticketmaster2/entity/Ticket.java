package de.hohenheim.ticketmaster2.entity;

import javax.persistence.*;

@Entity
public class Ticket {
    @Id
    @GeneratedValue
    private Integer number;

    private String categorization;

    private String problemSpecification;

    @ManyToOne
    @JoinColumn(name = "adminId")
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
