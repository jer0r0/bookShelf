package com.challenges.bookshelf.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public record Datos(List<DatosLibro> libros) {
    public Datos(@JsonAlias({"results"}) List<DatosLibro> libros) {

        this.libros = libros;
    }

    @JsonAlias({"results"})
    public List<DatosLibro> libros() {
        return this.libros;
    }


}
