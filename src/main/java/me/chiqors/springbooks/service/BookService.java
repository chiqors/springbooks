package me.chiqors.springbooks.service;

import me.chiqors.springbooks.dto.BookDTO;
import me.chiqors.springbooks.model.Book;
import me.chiqors.springbooks.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;

    // ------------------- CONSTRUCTOR -------------------

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // ------------------- CONVERTER -------------------

    public BookDTO convertToDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        // bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setStock(book.getStock());
        bookDTO.setPublishedAt(book.getPublishedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        bookDTO.setRegisteredAt(book.getRegisteredAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        bookDTO.setDeleted(book.isDeleted());
        bookDTO.setBookCode(book.getBookCode());
        return bookDTO;
    }

    public Book convertToEntity(BookDTO bookDTO) {
        Book book = new Book();
        // book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setStock(bookDTO.getStock());
        book.setPublishedAt(LocalDate.parse(bookDTO.getPublishedAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        book.setRegisteredAt(LocalDate.parse(bookDTO.getRegisteredAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        book.setDeleted(bookDTO.isDeleted());
        book.setBookCode(bookDTO.getBookCode());
        return book;
    }

    // ------------------- CRUD -------------------

    public List<BookDTO> getAllBooks(String title) {
        List<Book> books;
        if (title != null) {
            books = bookRepository.findByTitleContainingIgnoreCaseAndDeletedIsFalse(title);
        } else {
            books = bookRepository.findAllByDeletedIsFalse();
        }

        return books.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public BookDTO getBookById(long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            return convertToDTO(book);
        }
        return null;
    }

    public BookDTO getBookByCode(String code) {
        Book book = bookRepository.findByBookCodeAndDeletedIsFalse(code);
        if (book != null) {
            return convertToDTO(book);
        }
        return null;
    }

    public BookDTO addBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setStock(bookDTO.getStock());
        book.setPublishedAt(LocalDate.parse(bookDTO.getPublishedAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        book.setRegisteredAt(LocalDate.parse(bookDTO.getRegisteredAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        book.setDeleted(false);
        // generate book code, format: B<day><month><year><id>
        String bookCode = "B" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + book.getId();
        book.setBookCode(bookCode);

        book = bookRepository.save(book);

        return convertToDTO(book);
    }


    public BookDTO updateBook(String code, BookDTO bookDTO) {
        Book book = bookRepository.findByBookCodeAndDeletedIsFalse(code);
        if (book != null) {
            book.setTitle(bookDTO.getTitle());
            book.setAuthor(bookDTO.getAuthor());
            book.setStock(bookDTO.getStock());
            book.setPublishedAt(LocalDate.parse(bookDTO.getPublishedAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            book.setRegisteredAt(LocalDate.parse(bookDTO.getRegisteredAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            book.setDeleted(bookDTO.isDeleted());

            book = bookRepository.save(book);

            return convertToDTO(book);
        }
        return null;
    }

    public boolean deleteBook(String code) {
        Book book = bookRepository.findByBookCodeAndDeletedIsFalse(code);
        if (book != null) {
            book.setDeleted(true);
            bookRepository.save(book);
            return true;
        }
        return false;
    }
}
