package de.hohenheim.ticketmaster2.repository;

import de.hohenheim.ticketmaster2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {
}
