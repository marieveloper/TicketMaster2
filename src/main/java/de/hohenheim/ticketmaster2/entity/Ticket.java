package de.hohenheim.ticketmaster2.entity;

import de.hohenheim.ticketmaster2.enums.IncidentCategorization;
import de.hohenheim.ticketmaster2.enums.Prioritization;
import de.hohenheim.ticketmaster2.enums.Status;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Entity
public class Ticket {
    @Id
    @GeneratedValue
    private Integer ticketId;

    @Enumerated(EnumType.STRING)
    private IncidentCategorization categorization;


    @Enumerated(EnumType.STRING)
    private Prioritization prio;


    private Timestamp creationTime;

    public Timestamp getRequestTime() {
        return requestTime;
    }

    private Timestamp requestTime;


    private Status status;

    private String content;

    private String title;

    @OneToMany(mappedBy = "ticket")
    private Set<Notification> notifications;

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

    public Set<Notification> getMessages() {
        return notifications;
    }



    public User getUser() {
        return user;
    }

    public void setTicketId(Integer number) {
        this.ticketId = number;
    }

    public void setCategorizationStr(String categorizationStr) {
        categorizationStr = categorizationStr.toUpperCase();
        try {
            this.categorization = IncidentCategorization.valueOf(categorizationStr);
        } catch (Exception e) {
            this.categorization = IncidentCategorization.UNKNOWN;
        }
    }

    public void setCategorization(IncidentCategorization categorization) {
        this.categorization = categorization;
    }

    public Status getStatus() {
        return status;
    }

    public void setPrio(Prioritization prio) {
        /*switch(prio){
            case LOW: System.out.println("less important");
                break;
            case MEDIUM: System.out.println("important");
                break;
            case HIGH: System.out.println("urgent");
        }*/
        this.prio = prio;
    }

    public void setStatus(Status status) {
      /*  switch(status){
            case IN_PROCESS: System.out.println("in process");
                break;
            case OPEN: System.out.println("open");
                break;
            case CLOSED: System.out.println("closed");
        }*/
        this.status = status;
    }
    public void setMessages(Set<Notification> notifications) {
        this.notifications = notifications;
    }


    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", categorization=" + categorization +
                ", prio=" + prio +
                ", timestamp=" + creationTime +
                ", status=" + status +
                ", notifications=" + notifications +
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
    public String getCreationTimeString(Timestamp timestamp){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd, HH:mm:ss").format(timestamp);
        return timeStamp;
    }


    public void setCreationTime(Timestamp timestamp) {
        this.creationTime = timestamp;
        this.requestTime = timestamp;
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

    public void setRequestTime(Timestamp requestTime) {
        this.requestTime = requestTime;
    }
}



