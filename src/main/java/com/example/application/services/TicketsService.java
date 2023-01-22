package com.example.application.services;

import com.example.application.entities.Ticket;
import com.example.application.repos.TicketsRepo;
import org.springframework.stereotype.Service;

@Service
public class TicketsService {

    private TicketsRepo repo;

    public TicketsService(TicketsRepo repo) {
        this.repo = repo;
    }

    public Ticket save(Ticket ticket) throws Exception {
        // check if there are remaining seats
        if(repo.countByPeriod(ticket.getPeriod()) == ticket.getPeriod().getNrOfSeats()) {
            throw new Exception("No more seats remaining!");
        }

        // check if seat is taken
        if(repo.findBySeatAndPeriod(ticket.getSeat(), ticket.getPeriod()) != null) {
            throw new Exception("Seat "+ticket.getSeat()+" is already taken!");
        }

        return repo.save(ticket);
    }
}
