package de.hohenheim.ticketmaster2.entity;

import javax.persistence.*;
import de.hohenheim.ticketmaster2.enums.*;

import java.util.Set;

@Entity
public class Ticket {
    @Id
    @GeneratedValue
    private Integer ticketId;

    private IncidentCategorization categorization;
    private Priorization prio;
    @OneToMany(mappedBy = "ticket")
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

    public Integer getTicketId() {
        return ticketId;
    }

    public IncidentCategorization getCategorization() {
        return categorization;
    }

    public Priorization getPrio() {
        return prio;
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

    public void setTicketId(Integer number) {
        this.ticketId = number;
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

    public void setPrio(Priorization prio) {
        switch(prio){
            case LOW: System.out.println("weniger wichtig");
                break;
            case MEDIUM: System.out.println("wichtig");
                break;
            case HIGH: System.out.println("sehr wichtig");
        }
        this.prio = prio;
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
