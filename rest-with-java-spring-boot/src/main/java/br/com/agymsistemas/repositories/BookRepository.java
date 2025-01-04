package br.com.agymsistemas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.agymsistemas.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {}