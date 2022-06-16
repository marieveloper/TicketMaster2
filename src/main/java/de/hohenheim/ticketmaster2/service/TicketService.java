package de.hohenheim.ticketmaster2.service;

import de.hohenheim.ticketmaster2.entity.Ticket;
import de.hohenheim.ticketmaster2.entity.User;
import de.hohenheim.ticketmaster2.enums.IncidentCategorization;
import de.hohenheim.ticketmaster2.enums.Prioritization;
import de.hohenheim.ticketmaster2.enums.Status;
import de.hohenheim.ticketmaster2.repository.TicketRepository;
import org.hibernate.annotations.OnDelete;
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
        ticket.setTitle(title);
        ticket.setContent(content);
        ticket.setPrio(Prioritization.MEDIUM); //TODO: Automatische Priorisierung
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
        for(int i = 0; i< 50; i++){
            createTicket("ticket" + i, "Das ist ein Testticket", authorList.get(i % authorList.size()).getUsername(), incidentCategorizations.get(i % incidentCategorizations.size()), Timestamp.from(Instant.now().minus(12, ChronoUnit.HOURS)));
        }
    }


    public void deleteTicket(int ticketId){
        ticketRepository.delete(ticketRepository.getById(ticketId)); //TODO: FK-Beziehungen mit @OnDelete versehen
    }

    public boolean canRequestStatus(int ticketId){
        Timestamp timestamp = getByTicketId(ticketId).getCreationTime();
        long deltaTime = System.currentTimeMillis() - timestamp.getTime();
        if(deltaTime / 3600000 >= 12){
            return true;
        }
        return false;
    }

    public void requestStatus(){
        //TODO: Comment/Benachrichtigung erzeugen
    }

    public void changeTicketPriority(int ticketId, Prioritization prio){
        Ticket ticket = getByTicketId(ticketId);
        ticket.setPrio(prio);
        ticketRepository.save(ticket);
    }

}
