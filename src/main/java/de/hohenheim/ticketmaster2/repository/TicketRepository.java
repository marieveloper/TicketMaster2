package de.hohenheim.ticketmaster2.repository;

import de.hohenheim.ticketmaster2.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
}
