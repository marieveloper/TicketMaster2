package de.hohenheim.ticketmaster2.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.hohenheim.ticketmaster2.enums.IncidentCategorization;
import de.hohenheim.ticketmaster2.enums.Prioritization;
import de.hohenheim.ticketmaster2.enums.Status;
import de.hohenheim.ticketmaster2.repository.UserRepository;
import de.hohenheim.ticketmaster2.service.TicketService;
import de.hohenheim.ticketmaster2.service.UserService;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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


    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "adminId")
    private User responsibleAdmin;

    public Ticket() {
        // empty constructor for Hibernate
    }

    public Ticket(IncidentCategorization categorization, Prioritization prio, Timestamp creationTime, Timestamp requestTime, Status status, String content, String title, User user, User responsibleAdmin) {
        this.categorization = categorization;
        this.prio = prio;
        this.creationTime = creationTime;
        this.requestTime = requestTime;
        this.status = status;
        this.content = content;
        this.title = title;
        this.user = user;
        this.responsibleAdmin = responsibleAdmin;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public IncidentCategorization getCategorization() {
        return categorization;
    }

    public Prioritization getPrio() {
        return prio;
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
        switch (categorization) {
            case INACTIVITY:
                this.prio = Prioritization.MEDIUM;
                break;
            case TECHNICAL_PROBLEMS:
                this.prio = Prioritization.HIGH;
                break;
            case OTHER:
                this.prio = Prioritization.LOW;
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


    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", categorization=" + categorization +
                ", prio=" + prio +
                ", timestamp=" + creationTime +
                ", status=" + status +
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
     * @return a Timestamp of the creation time. You should probably not use this, use the one that returns a String
     */
    public Timestamp getCreationTime() {
        return this.creationTime;
    }

    public String getCreationTimeString(Timestamp timestamp) {
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


    public boolean canRequestStatus() {
        Timestamp timestamp = this.getRequestTime();
        long deltaTime = System.currentTimeMillis() - timestamp.getTime();
        if (deltaTime / 3600000 >= 12) {
            return true;
        }
        return false;
    }
   /* public void setResponsibleAdminAuto() {
        UserRepository userRepository;
        if (this.getCategorization() == IncidentCategorization.TECHNICAL_PROBLEMS) {
            if (this.getTicketId() % 2 == 0) {
                this.setResponsibleAdmin(admin);
            } else {
                this.setResponsibleAdmin(2);
            }
        }
        if (this.getCategorization() == IncidentCategorization.INACTIVITY) {
            if (this.getTicketId() % 2 == 0) {
                this.setResponsibleAdmin(userRepository.findByUsername("admin"));
            } else {
                this.setResponsibleAdmin(1);
            }
        }
        if (this.getCategorization() == IncidentCategorization.OTHER) {
            if (this.getTicketId() % 2 == 0) {
                this.setResponsibleAdmin(admin);
            } else {
                this.setResponsibleAdmin(3);
            }
        }
    }*/
}



