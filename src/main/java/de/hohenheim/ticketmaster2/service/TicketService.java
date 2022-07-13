package de.hohenheim.ticketmaster2.service;

import de.hohenheim.ticketmaster2.entity.Ticket;
import de.hohenheim.ticketmaster2.entity.User;
import de.hohenheim.ticketmaster2.enums.IncidentCategorization;
import de.hohenheim.ticketmaster2.enums.Status;
import de.hohenheim.ticketmaster2.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public List<Ticket> findAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getByTicketId(Integer id){
        return ticketRepository.getById(id);
    }

    public List<Ticket> getAllTicketsByAuthor(String username){
        return findAllTickets().stream().filter(t -> t.getUser().getUsername().equals(username)).toList();
    }

    public List<Ticket> getAllTicketsByUserId(Integer id){
        return findAllTickets().stream().filter(t -> t.getUser().getUserId()==id).toList();
    }

    public List<Ticket> getAllTicketsByAdminId(Integer id){
        return findAllTickets().stream().filter(t -> t.getResponsibleAdmin().getUserId()==id).toList();
    }

    public List<Ticket> getAllTicketsWithStatus(Status status){
        return findAllTickets().stream().filter(t -> t.getStatus() == status).toList();
    }

    public List<Ticket> getAllTicketsByAdmin(String username){
        return findAllTickets().stream().filter(t -> t.getResponsibleAdmin().equals(username)).toList();
    }

    public void createTicket(String title, String content, String authorName, IncidentCategorization incidentCategorization, Timestamp timestamp){
        Ticket ticket = new Ticket();
        ticket.setCreationTime(timestamp);
        ticket.setStatus(Status.OPEN);
        ticket.setCategorization(incidentCategorization);
        ticket.setResponsibleAdmin(userService.getUserByUsername("admin"));
        //TODO!
        this.setResponsibleAdminAuto(ticket);
        ticket.setTitle(title);
        ticket.setContent(content);
        ticket.setPrioAuto();
        ticket.setUser(userService.getUserByUsername(authorName));
        ticketRepository.save(ticket);
    }


    /**
     * Erzeugt 50 Test tickets, die 12h in der Vergangenheit liegen
     *
     * @param authorList
     */
    public void createTestTickets(List<User> authorList){
        List<IncidentCategorization> incidentCategorizations = List.of(IncidentCategorization.INACTIVITY, IncidentCategorization.OTHER, IncidentCategorization.TECHNICAL_PROBLEMS);
        List<String> titles = List.of("User xy inactive", "No delivery", "Edit ticket defect");
        for(int i = 0; i< 49; i++){
            createTicket(titles.get(i % titles.size()), "This is a test ticket", authorList.get(i % authorList.size()).getUsername(), incidentCategorizations.get(i % incidentCategorizations.size()), Timestamp.from(Instant.now().minus(12, ChronoUnit.HOURS)));
        }
        createTicket(titles.get(49 % titles.size()), "This is a test ticket", "user", incidentCategorizations.get(49 % incidentCategorizations.size()), Timestamp.from(Instant.now()));
    }


    public void deleteTicket(int ticketId){
        ticketRepository.delete(ticketRepository.getById(ticketId));
    }

    public boolean canRequestStatus(int ticketId){
        Timestamp timestamp = getByTicketId(ticketId).getRequestTime();
        long deltaTime = System.currentTimeMillis() - timestamp.getTime();
        if(deltaTime / 3600000 >= 12){
            return true;
        }
        return false;
    }

    public void changeTicketPriority(int ticketId){
        Ticket ticket = getByTicketId(ticketId);
        ticket.setPrioAuto();
        ticketRepository.save(ticket);
    }
    public void changeTicketStatus(int ticketId, Status status){
        Ticket ticket = getByTicketId(ticketId);
        ticket.setStatus(status);
        ticketRepository.save(ticket);
    }
public void changeTicketResponsibleAdmin(int ticketId, String username){
        Ticket ticket = getByTicketId(ticketId);
        ticket.setResponsibleAdmin(userService.getUserByUsername(username));
        ticketRepository.save(ticket);
    }
    public void changeTicketIncidentCategorization(int ticketId, IncidentCategorization incidentCategorization){
        Ticket ticket = getByTicketId(ticketId);
        ticket.setCategorization(incidentCategorization);
        ticketRepository.save(ticket);
    }


    public void add(Ticket ticket){


        ticketRepository.save(ticket);
    }

    public List<Ticket> findByKeyword(String keyword){
        return ticketRepository.findByKeyword(keyword);
    }
    public List<Ticket> findByFilterKeyword(String FilterKeyword){
        return ticketRepository.findByFilterKeyword(FilterKeyword);
    }
    public void setResponsibleAdminAuto(Ticket ticket) {
        ticketRepository.save(ticket);
        if (ticket.getCategorization() == IncidentCategorization.TECHNICAL_PROBLEMS) {
            if (ticket.getTicketId() % 2 == 0) {
                ticket.setResponsibleAdmin(userService.getUserByUsername("admin"));
            } else {
                ticket.setResponsibleAdmin(userService.getUserByUsername("Merkel"));
            }
        }
        if (ticket.getCategorization() == IncidentCategorization.INACTIVITY) {
            if (ticket.getTicketId() % 2 == 0) {
                ticket.setResponsibleAdmin(userService.getUserByUsername("admin"));
            } else {
                ticket.setResponsibleAdmin(userService.getUserByUsername("Trump"));
            }
        }
        if (ticket.getCategorization() == IncidentCategorization.OTHER) {
            if (ticket.getTicketId() % 2 == 0) {
                ticket.setResponsibleAdmin(userService.getUserByUsername("admin"));
            } else {
                ticket.setResponsibleAdmin(userService.getUserByUsername("Obama"));
            }
        }
    }


}
