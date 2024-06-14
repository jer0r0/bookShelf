package com.challenges.bookshelf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

@Entity
@Table(name = "autores")

public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Libro> libros;
    private String nombre;
    private Long fechaNacimiento;
    private Long fechaDeMuerte;

    public Autor() {}

    public Autor(DatosAutor datosAutor) {

        this.nombre = datosAutor.nombre();
        this.fechaNacimiento = datosAutor.fechaDeNacimiento();
        this.fechaDeMuerte = datosAutor.fechaDeMuerte();
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros){
        libros.forEach(e->e.setAutor(this));
        this.libros = libros;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {return nombre;}
    public void setNombre(String nombre){this.nombre = nombre;}
    public Long getFechaNacimiento() {return fechaNacimiento;}
    public void setFechaNacimiento(Long fechaNacimiento) {this.fechaNacimiento = fechaNacimiento;}
    public Long getFechaDeMuerte() {return fechaDeMuerte;}
    public void setFechaDeMuerte(Long fechaDeMuerte) {this.fechaDeMuerte = fechaDeMuerte;}

    @Override
    public String toString() {
        return nombre + ", ("+fechaNacimiento+"). ";
    }
}
