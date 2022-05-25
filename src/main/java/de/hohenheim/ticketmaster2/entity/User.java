package de.hohenheim.ticketmaster2.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Integer userId;

    private String username;

    private String password;

    private String mail;

    private Date lastLogin;

    private String writingPermission;

    private String configurations;

    @OneToMany(mappedBy = "user")
    private List<Ticket> tickets = new ArrayList<>();

    public String getConfigurations() {
        return configurations;
    }

    public void setConfigurations(String configurations) {
        this.configurations = configurations;
    }

    public String getWritingPermission() {
        return writingPermission;
    }

    public void setWritingPermission(String permission) {
        this.writingPermission = permission;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public User() {
        // empty constructor for Hibernate
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
