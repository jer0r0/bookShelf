package com.challenges.bookshelf.principal;

import com.challenges.bookshelf.model.*;


import com.challenges.bookshelf.repository.AutorRepository;
import com.challenges.bookshelf.repository.LibroRepository;
import com.challenges.bookshelf.service.ConsumoAPI;
import com.challenges.bookshelf.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private Scanner scanner = new Scanner(System.in);
    private LibroRepository repository;
    private AutorRepository repositoryAutor;


    public Principal(LibroRepository repository, AutorRepository repositoryAutor) {
        this.repository = repository;
        this.repositoryAutor = repositoryAutor;
    }

    public void muestraMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por titulo
                    2 - listar libros registrados
                    3 - listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma 
                                       
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    mostrarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("Escribe el idioma para listar libros:");
        System.out.println(
                "es- Español\n" +
                "fr- Frances\n" +
                "en- Inglés\n" +
                "pt- Portugués\n "
        );
        String idioma = scanner.nextLine();

        List<Libro> libros = repository.findLibrosByIdioma(idioma);
        if (libros.isEmpty()) {
            System.out.println("No hay libros en el idioma " + idioma + ".");
        } else {
            System.out.println("Libros en el idioma " + idioma + ":");
            for (Libro libro : libros) {
                System.out.println(libro);
            }
        }
    }


    private void listarAutoresVivos() {
        System.out.println("Escribe el año para buscar autores vivos:");
        int year = scanner.nextInt();
        scanner.nextLine();

        List<Autor> autores = repositoryAutor.findAutoresVivosEnAno(year);
        if (autores.isEmpty()) {
            System.out.println("No hay autores vivos en el año " + year + ".");
        } else {
            System.out.println("Autores vivos en el año " + year + ":");
            for (Autor autor : autores) {
                System.out.println("Autor: " + autor.getNombre() + " (Nacimiento: " + autor.getFechaNacimiento() + ", Muerte: " + (autor.getFechaDeMuerte() != null ? autor.getFechaDeMuerte() : "Vivo") + ")");
            }
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = repositoryAutor.findAllWithLibros();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            System.out.println("Autores con sus libros:");
            for (Autor autor : autores) {
                System.out.println("Autor: " + autor.getNombre());
                List<Libro> libros = autor.getLibros();
                if (libros.isEmpty()) {
                    System.out.println("  No tiene libros registrados.");
                } else {
                    for (Libro libro : libros) {
                        System.out.println("  Libro: " + libro.getTitulo());
                    }
                }
            }
        }
    }

    private void mostrarLibrosRegistrados() {
        List<Libro> libros = repository.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            System.out.println("Libros registrados:");
            libros.stream()
                    .sorted(Comparator.comparing(Libro::getTitulo))
                    .forEach(System.out::println);
        }
    }


    public void buscarLibroPorTitulo() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var nombreLibro = scanner.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));

        Datos datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println(datos);

        Optional<DatosLibro> libroBuscado = datos.libros().stream()
                .filter(l -> l.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst();

        try {
            if (libroBuscado.isPresent()) {
                System.out.println("Libro encontrado");

                DatosLibro datosLibro = libroBuscado.get();
                Libro libro = new Libro(datosLibro);

                if (!datosLibro.autores().isEmpty()) {
                    DatosAutor datosAutor = datosLibro.autores().get(0);
                    Optional<Autor> autorExistente = repositoryAutor.findByNombre(datosAutor.nombre());
                    if (autorExistente.isPresent()) {
                        libro.setAutor(autorExistente.get());
                    } else {
                        Autor nuevoAutor = new Autor(datosAutor);
                        repositoryAutor.save(nuevoAutor);
                        libro.setAutor(nuevoAutor);
                    }
                }
                repository.save(libro);
                System.out.println(libro);
            } else {
                System.out.println("Libro no encontrado");
            }
        } catch (DataIntegrityViolationException e) {
            System.out.println("El libro ya fue agregado.\n ");
            System.out.println(e.getMessage());
        }
    }
}
