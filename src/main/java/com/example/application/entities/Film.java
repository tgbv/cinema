package com.example.application.entities;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "films")
@Transactional
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    private Integer id;

    @NotNull
    @Column(unique = true)
    private String title;

    @NotNull
    private LocalDate debutDate;

    @ManyToOne
    private FilmAuthor author;

    @OneToMany(mappedBy = "film", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<BroadcastPeriod> broadcastPeriods;

    public Film() {}


    public Film(String title, LocalDate debutDate, FilmAuthor author) {
        this.title = title;
        this.debutDate = debutDate;
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDebutDate() {
        return debutDate;
    }

    public String getDebutDateStr() {
        return debutDate.toString();
    }

    public void setDebutDate(LocalDate debutDate) {
        this.debutDate = debutDate;
    }

    public FilmAuthor getAuthor() {
        return author;
    }

    public void setAuthor(FilmAuthor author) {
        this.author = author;
    }

    public List<BroadcastPeriod> getBroadcastPeriods() {
        return broadcastPeriods;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
