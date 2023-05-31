package me.chiqors.springbooks.service;

import me.chiqors.springbooks.dto.BookDTO;
import me.chiqors.springbooks.model.Book;
import me.chiqors.springbooks.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Service class for handling Book-related operations.
 */
@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    /**
     * Converts a Book entity to a BookDTO.
     *
     * @param book the Book entity
     * @return the corresponding BookDTO
     */
    public BookDTO convertToDTO(Book book) {
        String publishedAt = new SimpleDateFormat("yyyy-MM-dd").format(book.getPublishedAt());
        String registeredAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(book.getRegisteredAt());
        String updatedAt = book.getUpdatedAt() != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(book.getUpdatedAt()) : null;

        return new BookDTO(book.getTitle(), book.getAuthor(), book.getStock(), publishedAt, registeredAt, book.getBookCode(), updatedAt);
    }

    /**
     * Retrieves all books with optional title filtering and pagination.
     *
     * @param title the title filter (optional)
     * @param page  the page number for pagination
     * @param size  the page size for pagination
     * @return the page of BookDTOs
     */
    public Page<BookDTO> getAllBooks(String title, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Book> bookPage;

        if (title != null) {
            bookPage = bookRepository.findByTitleContainingIgnoreCaseAndDeletedIsFalseOrderByRegisteredAtDesc(title, pageable);
        } else {
            bookPage = bookRepository.findByDeletedIsFalseOrderByRegisteredAtDesc(pageable);
        }

        return bookPage.map(this::convertToDTO);
    }

    /**
     * Retrieves a book by its book code.
     *
     * @param code the book code
     * @return the corresponding BookDTO
     */
    public BookDTO getBookByCode(String code) {
        Book book = bookRepository.findByBookCodeAndDeletedIsFalse(code);
        if (book != null) {
            return convertToDTO(book);
        }
        return null;
    }

    public boolean isValidBookCode(String bookCode) {
        Book book = bookRepository.findByBookCodeAndDeletedIsFalse(bookCode);
        return book != null;
    }

    /**
     * Adds a new book.
     *
     * @param bookDTO the BookDTO containing the book details
     * @return the added BookDTO
     */
    public BookDTO addBook(BookDTO bookDTO) {
        Book book = new Book();

        // Generate book code: B<day><month><year><HH><mm><ss><A-Z>
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyHHmmss");
        String generateChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String bookCode = "B" + formatter.format(new Date()) + generateChar.charAt((int) (Math.random() * generateChar.length()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date publishedAt = dateFormat.parse(bookDTO.getPublishedAt());
            Date registeredAt = new Date();
            Date updatedAt = bookDTO.getUpdatedAt() != null ? dateFormat.parse(bookDTO.getUpdatedAt()) : null;

            book = new Book(bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getStock(), publishedAt, registeredAt, bookCode, updatedAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        book = bookRepository.save(book);

        return convertToDTO(book);
    }

    /**
     * Updates a book by its book code.
     *
     * @param bookDTO  the updated BookDTO
     * @return the updated BookDTO
     */
    public BookDTO updateBook(BookDTO bookDTO) {
        Book book = bookRepository.findByBookCodeAndDeletedIsFalse(bookDTO.getBookCode());
        if (book != null) {
            if (bookDTO.getTitle() != null) {
                book.setTitle(bookDTO.getTitle());
            }
            if (bookDTO.getAuthor() != null) {
                book.setAuthor(bookDTO.getAuthor());
            }
            if (bookDTO.getStock() != null) {
                book.setStock(bookDTO.getStock());
            }
            if (bookDTO.getPublishedAt() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date publishedAt = dateFormat.parse(bookDTO.getPublishedAt());
                    book.setPublishedAt(publishedAt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            Date updatedAt = new Date();
            book.setUpdatedAt(updatedAt);

            bookRepository.save(book);

            return convertToDTO(book);
        }
        return null;
    }

    /**
     * Deletes a book by its book code.
     *
     * @param code the book code
     * @return true if the book was deleted successfully, false otherwise
     */
    public boolean deleteBook(String code) {
        Book book = bookRepository.findByBookCodeAndDeletedIsFalse(code);
        if (book != null) {
            book.setDeleted(true);
            book.setDeletedAt(new Date());

            bookRepository.save(book);

            return true;
        }
        return false;
    }
}