package me.chiqors.springbooks.service;

import me.chiqors.springbooks.dto.BookDTO;
import me.chiqors.springbooks.model.Book;
import me.chiqors.springbooks.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookDTO convertToDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setStock(book.getStock());
        bookDTO.setPublishedAt(book.getPublishedAt());
        bookDTO.setRegisteredAt(book.getRegisteredAt());
        bookDTO.setDeleted(book.isDeleted());
        return bookDTO;
    }

    public List<BookDTO> getAllBooks(String title) {
        List<Book> books;
        if (title == null) {
            books = bookRepository.findAll();
        } else {
            books = bookRepository.findByTitleContaining(title);
        }
        return books.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> getPagedBooks(int page, int size) {
        List<Book> books = bookRepository.findAll(PageRequest.of(page, size)).toList();
        return books.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookDTO getBookById(long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            return convertToDTO(book);
        }
        return null;
    }

    public BookDTO addBook(BookDTO bookDTO) {
        Book book = new Book(
                bookDTO.getTitle(),
                bookDTO.getAuthor(),
                bookDTO.getStock(),
                bookDTO.getPublishedAt(),
                bookDTO.getRegisteredAt()
        );
        book = bookRepository.save(book);
        return convertToDTO(book);
    }

    public BookDTO updateBook(long id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            book.setTitle(bookDTO.getTitle());
            book.setAuthor(bookDTO.getAuthor());
            book.setStock(bookDTO.getStock());
            book.setPublishedAt(bookDTO.getPublishedAt());
            book.setRegisteredAt(bookDTO.getRegisteredAt());
            book = bookRepository.save(book);
            return convertToDTO(book);
        }
        return null;
    }

    public BookDTO deleteBook(long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            book.setDeleted(true);
            book = bookRepository.save(book);
            return convertToDTO(book);
        }
        return null;
    }
}
