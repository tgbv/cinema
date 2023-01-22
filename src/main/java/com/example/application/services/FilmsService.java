package com.example.application.services;

import com.example.application.entities.BroadcastPeriod;
import com.example.application.entities.Film;
import com.example.application.entities.FilmAuthor;
import com.example.application.repos.FilmsRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FilmsService {
    private FilmsRepo filmsRepo;

    public FilmsService(FilmsRepo filmsRepo) {
        this.filmsRepo = filmsRepo;
    }

    public List<Film> findAll() {
        return this.filmsRepo.findAll();
    }

    public Film create(String title, LocalDate debutDate, FilmAuthor filmAuthor) {
        return filmsRepo.save(new Film(title, debutDate, filmAuthor));
    }

    public void delete(Film film) {
        filmsRepo.delete(film);
    }

    public Film update(Film film) {
        return filmsRepo.save(film);
    }

}
