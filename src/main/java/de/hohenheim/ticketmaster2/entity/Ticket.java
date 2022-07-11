package de.hohenheim.ticketmaster2.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.hohenheim.ticketmaster2.enums.IncidentCategorization;
import de.hohenheim.ticketmaster2.enums.Prioritization;
import de.hohenheim.ticketmaster2.enums.Status;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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


    @Enumerated(EnumType.STRING)
    private Status status;

    private String content;

    private String title;

    @OneToMany(mappedBy = "ticket")
    @JsonManagedReference
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "ticket")
    private Set<Message> messages;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "adminId")
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

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public Set<Message> getMessages() {return messages;}

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
            this.categorization = IncidentCategorization.OTHER;
        }
    }

    public void setCategorization(IncidentCategorization categorization) {
        this.categorization = categorization;
    }

    public Status getStatus() {
        return status;
    }

    public void setPrioAuto() {
        switch(categorization){
            case INACTIVITY: this.prio = Prioritization.MEDIUM;
                break;
            case TECHNICAL_PROBLEMS: this.prio =Prioritization.HIGH;
                break;
            case OTHER: this.prio =Prioritization.LOW;
                break;
        }
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
    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    public void setMessages(Set<Message> messages) {this.messages = messages; }

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

    public void setPrio(Prioritization prio) {
        this.prio = prio;
    }


    public boolean canRequestStatus(){
        Timestamp timestamp = this.getRequestTime();
        long deltaTime = System.currentTimeMillis() - timestamp.getTime();
        if(deltaTime / 3600000 >= 12){
            return true;
        }
        return false;
    }
}



