package com.example.application.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tickets")
@Transactional
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    private Integer id;

    @NotNull
    @ManyToOne
    @Autowired
    private BroadcastPeriod period;

    @NotNull
    private Double price;

    /**
     * Can be null, if ticket wasn't bought by a user.
     */
    @ManyToOne
    private User user;

    @NotNull
    private String seat;


    public Ticket() {
    }

    public Ticket(BroadcastPeriod period, Double price, User user, String seat) {
        this.period = period;
        this.price = price;
        this.user = user;
        this.seat = seat;
    }

    public Integer getId() {
        return id;
    }

    public BroadcastPeriod getPeriod() {
        return period;
    }

    public void setPeriod(BroadcastPeriod period) {
        this.period = period;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }


    private String qr;
    public String getQr() {
        return id + "" + seat;
    }
}
