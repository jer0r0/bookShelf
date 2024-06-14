package com.challenges.bookshelf;

import com.challenges.bookshelf.principal.Principal;
import com.challenges.bookshelf.repository.LibroRepository;
import com.challenges.bookshelf.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookshelfApplication implements CommandLineRunner {

	@Autowired
	private LibroRepository repository;

	@Autowired
	private AutorRepository AutorRepository;


	public static void main(String[] args) {
		SpringApplication.run(BookshelfApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal main = new Principal(repository,AutorRepository);
		main.muestraMenu();
	}
}
