package com.example.application.services;

import com.example.application.entities.BroadcastPeriod;
import com.example.application.entities.Film;
import com.example.application.repos.BroadcastPeriodsRepo;
import com.example.application.repos.DirectRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@Service
public class BroadcastPeriodsService {

    private BroadcastPeriodsRepo repo;

    public BroadcastPeriodsService(BroadcastPeriodsRepo repo) {
        this.repo = repo;
    }

    /**
     *
     * @param startTime
     * @param endTime
     * @param nrOfSeats
     * @param film
     * @return
     */
    public BroadcastPeriod save(
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer nrOfSeats,
        Film film,
        Double pricePerSeat
    ) throws Exception {
        if(intervalOverlaps(startTime, endTime)) {
            throw new Exception("Interval overlaps!");
        }

        return repo.save(
            new BroadcastPeriod(
                Date.from(startTime.toInstant(ZoneOffset.of("+02:00"))),
                Date.from(endTime.toInstant(ZoneOffset.of("+02:00"))),
                film,
                nrOfSeats,
                pricePerSeat
            )
        );
    }

    public BroadcastPeriod update(BroadcastPeriod bp) throws Exception {
        if(intervalOverlapsExcept(bp.getStartTimeLDT(), bp.getEndTimeLDT(), bp.getId())) {
            throw new Exception("Interval overlaps with other registries !");
        }

        return repo.save(bp);
    }

    public void delete(BroadcastPeriod bp) {
        repo.delete(bp);
    }

    /**
     *
     * @return
     */
    public List<BroadcastPeriod> findAll() {
        return repo.findAll();
    }

    /**
     * Check if interval overlaps with another interval registered in database.
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public boolean intervalOverlaps(LocalDateTime startTime, LocalDateTime endTime) {
        return DirectRepo.firstBroadcastPeriod(startTime, endTime) != null;
    }

    /**
     * Check if interval overlaps with another interval registered in database, except for itself.
     *
     * @param startTime
     * @param endTime
     * @param broadcastIntervalId
     * @return
     */
    public boolean intervalOverlapsExcept(LocalDateTime startTime, LocalDateTime endTime, Integer broadcastIntervalId) {
        BroadcastPeriod bp = DirectRepo.firstBroadcastPeriod(startTime, endTime);

        if(bp == null) {
            return false;
        }

        return bp.getId() != broadcastIntervalId;
    }

}


