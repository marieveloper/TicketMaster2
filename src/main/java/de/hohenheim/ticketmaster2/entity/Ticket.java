package de.hohenheim.ticketmaster2.entity;

import de.hohenheim.ticketmaster2.enums.IncidentCategorization;
import de.hohenheim.ticketmaster2.enums.Prioritization;
import de.hohenheim.ticketmaster2.enums.Status;

import javax.persistence.*;
import java.time.LocalDate;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Set;

@Entity
public class Ticket {
    @Id
    @GeneratedValue
    private Integer ticketId;

    private IncidentCategorization categorization;
    private Prioritization prio;

    private Timestamp creationTime;
    private Status status;

    private String content;

    private String title;

    @OneToMany(mappedBy = "ticket")
    private Set<Message> messages;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    private User responsibleAdmin;

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
                ", timestamp=" + creationTime +
                ", status=" + status +
                ", messages=" + messages +
                ", user=" + user +
                '}';
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreationTime(String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(creationTime.toLocalDateTime());
    }

    /**
     *
     * @return a Timestamp of the creation time. You should probably not use this, use the one that returns a String
     */
    public Timestamp getCreationTime(){
        return this.creationTime;
    }


    public void setCreationTime(Timestamp timestamp) {
        this.creationTime = timestamp;
    }

    public User getResponsibleAdmin() {
        return responsibleAdmin;
    }

    public void setResponsibleAdmin(User responsibleAdmin) {
        this.responsibleAdmin = responsibleAdmin;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
