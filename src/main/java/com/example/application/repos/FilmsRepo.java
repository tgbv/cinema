package com.example.application.repos;

import com.example.application.entities.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmsRepo extends JpaRepository<Film, Integer> {

}
