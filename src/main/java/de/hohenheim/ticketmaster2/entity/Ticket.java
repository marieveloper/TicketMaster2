package de.hohenheim.ticketmaster2.entity;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

@Entity
public class Ticket {
    @Id
    @GeneratedValue
    private Integer number;

    private String categorization;
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

    public String getCategorization() {
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

    public void setCategorization(String categorization) {
        this.categorization = categorization;
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
