package de.hohenheim.ticketmaster2.service;

import de.hohenheim.ticketmaster2.entity.Role;
import de.hohenheim.ticketmaster2.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Component
public class TestDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(TestDataLoader.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private MessageService messageService;

    /**
     * Diese Methode wird zum Aufsetzen von Testdaten für die Datenbank verwendet werden. Die Methode wird immer dann
     * ausgeführt, wenn der Spring Kontext initialisiert wurde, d.h. wenn Sie Ihren Server (neu-)starten.
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("Initialisiere Datenbank mit Testdaten...");

        // Initialisieren Sie Beispielobjekte und speichern Sie diese über Ihre Services
        Role userRole = new Role("ROLE_USER");
        Role adminRole = new Role("ROLE_ADMIN");
        Role technicalAdminRole = new Role("ROLE_TECHNICAL_ADMIN");
        Role inactivityAdminRole = new Role("ROLE_INACTIVITY_ADMIN");
        Role otherAdminRole = new Role("ROLE_OTHER_ADMIN");
        roleService.saveRole(userRole);
        roleService.saveRole(adminRole);

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);

        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);

        List<User> users =new LinkedList<>();
        List<String> names = List.of("Trump", "Merkel", "Obama", "Potter", "Duck", "Master");

        for(int i =0; i<3; i++){
            userService.createUser(names.get(i),passwordEncoder.encode( i+"1000"), adminRole);

        }
        for(int i =3; i<6; i++){
            userService.createUser(names.get(i),passwordEncoder.encode( i+"1000"), userRole);
            users.add(userService.getUserByUsername(names.get(i)));
        }
        userService.getUserByUsername("Trump").getRoles().add(inactivityAdminRole);
        userService.getUserByUsername("Merkel").getRoles().add(technicalAdminRole);
        userService.getUserByUsername("Obama").getRoles().add(otherAdminRole);

        User normalUser = new User();
        normalUser.setUsername("user");
        normalUser.setPassword(passwordEncoder.encode("1234"));
        normalUser.setRoles(userRoles);
        normalUser.setWritingPermission(false);
        userService.saveUser(normalUser);

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRoles(adminRoles);
        userService.saveUser(admin);
        users.add(normalUser);

        //userService.createUser("a",passwordEncoder.encode("a"),adminRole);

        ticketService.createTestTickets(users);



    }
}
