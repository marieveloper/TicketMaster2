package de.hohenheim.ticketmaster2.entity;

import javax.persistence.*;
import de.hohenheim.ticketmaster2.enums.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

@Entity
public class Ticket {
    @Id
    @GeneratedValue
    private Integer number;

    private IncidentCategorization categorization;
    @Transient //TODO Error?
    private Set<Message> messages;

    @ManyToOne
    @JoinColumn(name = "adminId")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Ticket() {
        // empty constructor for Hibernate
    }

    public Integer getNumber() {
        return number;
    }

    public IncidentCategorization getCategorization() {
        return categorization;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public Role getRole() {
        return role;
    }

    public User getUser() {
        return user;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setCategorization(IncidentCategorization categorization) {
        switch(categorization){
            case INACTIVITY: System.out.println("User ist inaktiv");
            break;
            case TECHNICALPROBLEMS: System.out.println("Hardware/ Software Probleme");
            break;
            case OTHER: System.out.println("Sonstige");
            break;
        }
        this.categorization=categorization;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
