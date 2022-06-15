package de.hohenheim.ticketmaster2.entity;

import javax.persistence.*;
import de.hohenheim.ticketmaster2.enums.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
public class Ticket {
    @Id
    @GeneratedValue
    private Integer ticketId;

    private IncidentCategorization categorization;
    private Prioritization prio;

    private LocalDate date;
    private Status status;

    public LocalDate getDate() {
        return date;
    }

    public void setDate() {
        this.date = date.now();
    }

    @OneToMany(mappedBy = "ticket")
    private Set<Message> messages;

    @ManyToMany
    @JoinColumn(name = "adminId")
    private Set<Role> roles;

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

    public Prioritization getPrio() {return prio; }

    public Set<Message> getMessages() {
        return messages;
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
            case TECHNICAL_PROBLEMS: System.out.println("software/ hardware problems");
            break;
            case OTHER: System.out.println("other");
            break;
        }
        this.categorization=categorization;
    }

    public Status getStatus() {
        return status;
    }

    public void setPrio(Prioritization prio) {
        switch(prio){
            case LOW: System.out.println("less important");
                break;
            case MEDIUM: System.out.println("important");
                break;
            case HIGH: System.out.println("urgent");
        }
        this.prio = prio;
    }

    public void setStatus(Status status) {
        switch(status){
            case IN_PROCESS: System.out.println("in process");
                break;
            case OPEN: System.out.println("open");
                break;
            case CLOSED: System.out.println("closed");
        }
        this.status = status;
    }
    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }


    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", categorization=" + categorization +
                ", prio=" + prio +
                ", date=" + date +
                ", status=" + status +
                ", messages=" + messages +
                ", roles=" + roles +
                ", user=" + user +
                '}';
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
