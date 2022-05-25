package de.hohenheim.ticketmaster2.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Admin {
    @Id
    @GeneratedValue
    private Integer userId;

    private String username;

    private String password;

    private String mail;

    private Date lastLogin;

    private String writingPermission;

    private String readingPermission;

    private String configurations;

    private String specialization;

    @OneToMany(mappedBy = "admin")
    private List<Ticket> tickets = new ArrayList<>();

    public Admin() {
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getConfigurations() {
        return configurations;
    }

    public void setConfigurations(String configurations) {
        this.configurations = configurations;
    }

    public String getReadingPermission() {
        return readingPermission;
    }

    public void setReadingPermission(String readingPermission) {
        this.readingPermission = readingPermission;
    }

    public String getWritingPermission() {
        return writingPermission;
    }

    public void setWritingPermission(String writingPermission) {
        this.writingPermission = writingPermission;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


}
