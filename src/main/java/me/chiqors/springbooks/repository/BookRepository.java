package me.chiqors.springbooks.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import me.chiqors.springbooks.model.Book;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByDeletedIsFalse();
    List<Book> findByTitleContainingIgnoreCaseAndDeletedIsFalse(String title);

    Book findByBookCodeAndDeletedIsFalse(String bookCode);
}
