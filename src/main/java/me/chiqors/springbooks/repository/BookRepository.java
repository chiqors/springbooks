package me.chiqors.springbooks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import me.chiqors.springbooks.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllBooksByDeletedIsFalse();
    List<Book> findByTitleContainingAndDeletedIsFalse(String title);
}
