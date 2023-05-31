package me.chiqors.springbooks.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import me.chiqors.springbooks.model.Book;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByBookCodeAndDeletedIsFalse(String bookCode);

    Page<Book> findByTitleContainingIgnoreCaseAndDeletedIsFalseOrderByRegisteredAtDesc(String title, Pageable pageable);

    Page<Book> findByDeletedIsFalseOrderByRegisteredAtDesc(Pageable pageable);
}
