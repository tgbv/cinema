package com.example.application.repos;

import com.example.application.entities.BroadcastPeriod;
import com.example.application.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketsRepo extends JpaRepository<Ticket, Integer> {

    int countByPeriod(BroadcastPeriod bp);

    Ticket findBySeatAndPeriod(String seat, BroadcastPeriod bp);
}
