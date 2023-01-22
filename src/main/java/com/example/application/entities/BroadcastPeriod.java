package com.example.application.entities;

import jdk.jfr.Unsigned;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "broadcast_periods")
@Transactional
public class BroadcastPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    private Integer id;

    @DateTimeFormat
    @NotNull
    private Date startTime;

    @DateTimeFormat
    @NotNull
    private Date endTime;

    @ManyToOne
    private Film film;

    @Unsigned
    @ColumnDefault("0")
    private Integer nrOfSeats;

    @Unsigned
    @ColumnDefault("0")
    private Double pricePerSeat;

    @OneToMany(mappedBy = "period", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Ticket> tickets;

    public BroadcastPeriod() {
    }

    public BroadcastPeriod(Date startTime, Date endTime, Film film, Integer nrOfSeats, Double pricePerSeat) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.film = film;
        this.nrOfSeats = nrOfSeats;
        this.pricePerSeat = pricePerSeat;
    }

    public BroadcastPeriod(Integer id, Date startTime, Date endTime, Film film, Integer nrOfSeats, Double pricePerSeat) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.film = film;
        this.nrOfSeats = nrOfSeats;
        this.pricePerSeat = pricePerSeat;
    }

    public Integer getId() {
        return id;
    }

    public Date getStartTime() {
        return startTime;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getStartTimeLDT() {
        try {
            return LocalDateTime.parse(startTime.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
        } catch(Exception e) {
            return LocalDateTime.parse(startTime.toString(), DateTimeFormatter.ofPattern("EE LLL dd HH:mm:ss zzz yyyy"));
        }
    }

    public Date getEndTime() {
        return endTime;
    }

    /**
     *
     * @return
     */
    public LocalDateTime getEndTimeLDT() {
        try {
            return LocalDateTime.parse(endTime.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
        } catch(Exception e) {
            return LocalDateTime.parse(endTime.toString(), DateTimeFormatter.ofPattern("EE LLL dd HH:mm:ss zzz yyyy"));
        }
    }

    public void setStartTime(Date start) {
        this.startTime = start;
    }

    /**
     *
     * @param start
     */
    public void setStartTimeLDT(LocalDateTime start) {
        this.startTime = Date.from(start.toInstant(ZoneOffset.of("+02:00")));
    }

    public void setEndTime(Date end) {
        this.endTime = end;
    }

    /**
     *
     * @param end
     */
    public void setEndTimeLDT(LocalDateTime end) {
        this.endTime = Date.from(end.toInstant(ZoneOffset.of("+02:00")));
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Integer getNrOfSeats() {
        return nrOfSeats;
    }

    public void setNrOfSeats(Integer nrOfSeats) {
        this.nrOfSeats = nrOfSeats;
    }

    public Double getPricePerSeat() {
        return pricePerSeat == null ? 0.0 : pricePerSeat;
    }

    public void setPricePerSeat(Double pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "BroadcastPeriod{" +
            "id=" + id +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", film=" + film +
            ", nrOfSeats=" + nrOfSeats +
            ", pricePerSeat=" + pricePerSeat +
            '}';
    }

    public String toString(boolean pretty) {
        return this.startTime + " -> " + this.endTime;
    }

    /**
     * Shallow clone.
     *
     * @return
     */
    @Override
    public BroadcastPeriod clone() {
        return new BroadcastPeriod(
            this.id,
            this.startTime,
            this.endTime,
            this.film,
            this.nrOfSeats,
            this.pricePerSeat
        );
    }
}
