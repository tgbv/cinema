package com.example.application.repos;

import com.example.application.entities.BroadcastPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BroadcastPeriodsRepo extends JpaRepository<BroadcastPeriod, Integer> {
    @Override
    @Query("SELECT bp FROM BroadcastPeriod bp ORDER BY bp.startTime DESC")
    List<BroadcastPeriod> findAll();
}
