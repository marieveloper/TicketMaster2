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

    public List<Notification> findAllAdminNotifications(Integer adminId) {
        return findAllNotifications().stream().filter(t -> t.getReceiver().getUserId()==adminId).toList();
    }
    public List<Notification> findAllTicketNotifications(Integer ticketId) {
        return findAllNotifications().stream().filter(t -> t.getTicket().getTicketId()==ticketId).toList();
    }

    public List<Notification> findAllUnreadNotifications(List<Notification> notifications){
        return notifications.stream().filter(t -> t.isRead()==false).toList();
    }

    public void deleteByTicketId(Integer ticketId) {
        for (Notification notification: this.findAllTicketNotifications(ticketId)) {
            notificationRepository.delete(notification);
        }
    }
    public void deleteNotification(Notification notification){
            notificationRepository.delete(notification);
        }
public Notification getNotificationById(Integer id){return notificationRepository.getById(id);}


}
