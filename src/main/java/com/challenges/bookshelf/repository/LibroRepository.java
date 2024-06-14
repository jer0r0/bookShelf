package com.challenges.bookshelf.repository;

import com.challenges.bookshelf.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface LibroRepository extends JpaRepository<Libro,Long > {

    @Query("SELECT l FROM Libro l JOIN l.idiomas i WHERE i = :idioma")
    List<Libro> findLibrosByIdioma(String idioma);

}
