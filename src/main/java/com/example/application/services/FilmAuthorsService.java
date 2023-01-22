package com.example.application.services;

import com.example.application.entities.FilmAuthor;
import com.example.application.repos.FilmAuthorsRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FilmAuthorsService {

    private FilmAuthorsRepo repo;

    public FilmAuthorsService(FilmAuthorsRepo repo) {
        this.repo = repo;
    }

    public List<FilmAuthor> findAll() {
        return repo.findAll();
    }


    public FilmAuthor create(String firstName, String lastName, LocalDate birthDate) {
        return repo.save(new FilmAuthor(firstName, lastName, birthDate));
    }
}
