package com.example.application.repos;

import com.example.application.entities.FilmAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmAuthorsRepo extends JpaRepository<FilmAuthor, Integer> {
}
