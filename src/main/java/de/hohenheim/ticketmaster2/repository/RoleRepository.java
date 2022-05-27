package de.hohenheim.ticketmaster2.repository;

import de.hohenheim.ticketmaster2.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
