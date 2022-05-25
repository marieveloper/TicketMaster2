package de.hohenheim.ticketmaster2.service;


import de.hohenheim.ticketmaster2.entity.Admin;
import de.hohenheim.ticketmaster2.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public List<Admin> findAllAdmins() {
        return adminRepository.findAll();
    }
}
