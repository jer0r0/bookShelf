package com.challenges.bookshelf.model;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
        ignoreUnknown = true
)

public record DatosAutor(
        @JsonAlias("name") String nombre,
        @JsonAlias("birth_year") Long fechaDeNacimiento,
        @JsonAlias("death_year") Long fechaDeMuerte) {

    @Override
    public String toString() {
        return nombre + ". (" + fechaDeNacimiento + "). ";
    }
}
