package de.hohenheim.ticketmaster2.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class User {
    @Id
    @GeneratedValue
    private Integer userId;

    private String username;

    private String password;

    private boolean enabled = true;

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @OneToMany(mappedBy="author")

    private Set<Message> sendMessages;

    public Set<Message> getSendMessages() {
        return sendMessages;
    }

    public void setSendMessages(Set<Message> sendMessages) {
        this.sendMessages = sendMessages;
    }

    public Set<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(Set<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    @OneToMany(mappedBy="receiver")

    private Set<Message> receivedMessages;

    @OneToMany(mappedBy = "receiver")

    private Set<Notification> receivedNotifications;

    @OneToMany(mappedBy = "sender")

    private Set<Notification> sentNotifications;

    public Set<Notification> getReceivedNotifications() {
        return receivedNotifications;
    }

    public Set<Notification> getSentNotifications(){
        return sentNotifications;
    }

    public void setSentNotifications(Set<Notification> sentNotifications) {
        this.sentNotifications = sentNotifications;
    }

    public void setReceivedNotifications(Set<Notification> receivedNotifications) {
        this.receivedNotifications = receivedNotifications;
    }

    public User() {
        // empty constructor for Hibernate
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
