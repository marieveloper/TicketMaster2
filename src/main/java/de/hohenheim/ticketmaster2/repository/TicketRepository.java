package de.hohenheim.ticketmaster2.repository;

import de.hohenheim.ticketmaster2.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    @Query("select t from Ticket t where " +
            "t.user.username like %:keyword% OR " +
            "t.responsibleAdmin.username like %:keyword% OR " +
            "t.title like %:keyword% OR " +
            "upper(t.prio) like %:keyword% OR " +
            "upper(t.status) like %:keyword% OR " +
            "upper(t.categorization)  like %:keyword%")
    List<Ticket> findByKeyword(@Param("keyword") String keyword);
    @Query("select t from Ticket t where " +
            "t.user.username like %:filterKeyword% OR " +
            "t.responsibleAdmin.username like %:filterKeyword% OR " +
            "t.title like %:filterKeyword% OR " +
            "upper(t.prio) like %:filterKeyword% OR " +
            "upper(t.status) like %:filterKeyword% OR " +
            "upper(t.categorization)  like %:filterKeyword%")
    List<Ticket> findByFilterKeyword(@Param("filterKeyword") String FilterKeyword);
}