package com.challenges.bookshelf.service;
import org.springframework.boot.CommandLineRunner;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);

}
