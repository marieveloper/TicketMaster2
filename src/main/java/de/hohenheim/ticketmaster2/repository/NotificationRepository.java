package de.hohenheim.ticketmaster2.repository;

import de.hohenheim.ticketmaster2.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
