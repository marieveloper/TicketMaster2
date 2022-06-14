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
    private Priorisation prio;
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

    public Priorisation getPrio() {
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
            case INACTIVITY: System.out.println("user is inactive");
            break;
            case TECHNICALPROBLEMS: System.out.println("software/ hardware problems");
            break;
            case OTHER: System.out.println("other");
            break;
        }
        this.categorization=categorization;
    }

    public void setPrio(Priorisation prio) {
        switch(prio){
            case LOW: System.out.println("less important");
                break;
            case MEDIUM: System.out.println("important");
                break;
            case HIGH: System.out.println("urgent");
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
