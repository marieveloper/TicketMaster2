package de.hohenheim.ticketmaster2.service;

import de.hohenheim.ticketmaster2.entity.Notification;
import de.hohenheim.ticketmaster2.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public List<Notification> findAllNotifications() {
        return notificationRepository.findAll();
    }


}
