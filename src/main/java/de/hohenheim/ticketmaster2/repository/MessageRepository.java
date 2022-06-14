package de.hohenheim.ticketmaster2.repository;

import de.hohenheim.ticketmaster2.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
