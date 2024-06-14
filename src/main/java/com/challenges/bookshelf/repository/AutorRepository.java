package com.challenges.bookshelf.repository;

import com.challenges.bookshelf.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {


    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.libros")
    List<Autor> findAllWithLibros();

    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :year AND (a.fechaDeMuerte >= :year OR a.fechaDeMuerte IS NULL)")
    List<Autor> findAutoresVivosEnAno(int year);

    Optional<Autor> findByNombre(String nombre);
}
