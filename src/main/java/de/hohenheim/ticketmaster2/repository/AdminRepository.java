package de.hohenheim.ticketmaster2.repository;

import de.hohenheim.ticketmaster2.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
}
