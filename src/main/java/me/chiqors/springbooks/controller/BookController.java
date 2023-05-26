package me.chiqors.springbooks.controller;

import java.util.List;

import me.chiqors.springbooks.dto.BookDTO;

import me.chiqors.springbooks.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Retrieves all books based on optional filtering, sorting, and pagination parameters.
     * @param title    Optional parameter to filter books by title.
     * @param sort    Optional parameter to specify the sorting order (e.g., "asc" or "desc").
     * @param page    Optional parameter to specify the page number for pagination.
     * @param size    Optional parameter to specify the page size for pagination.
     * @return ResponseEntity containing a list of BookDTOs and an HTTP status code.
     */
    @GetMapping("/books")
    public ResponseEntity<?> getAllBooks(@RequestParam(required = false) String title,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer size) {
        try {
            List<BookDTO> bookDTOs = bookService.getAllBooks(title, sort, page, size);
            return new ResponseEntity<>(bookDTOs, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = "Failed to retrieve books";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a book by ID.
     * @param id    ID of the book to retrieve.
     * @return ResponseEntity containing a BookDTO and an HTTP status code.
     */
    @GetMapping("/book/{id}")
    public ResponseEntity<?> getBookById(@PathVariable("id") long id) {
        try {
            BookDTO bookDTO = bookService.getBookById(id);
            if (bookDTO != null) {
                return new ResponseEntity<>(bookDTO, HttpStatus.OK);
            } else {
                String errorMessage = "Book with id: " + id + " not found";
                return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            String errorMessage = "Failed to retrieve book with id: " + id;
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a new book.
     * @param bookDTO    BookDTO containing the book information to create.
     * @return ResponseEntity containing a BookDTO and an HTTP status code.
     */
    @PostMapping("/books")
    public ResponseEntity<?> createBook(@RequestBody BookDTO bookDTO) {
        try {
            BookDTO createdBookDTO = bookService.addBook(bookDTO);
            return new ResponseEntity<>(createdBookDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            String errorMessage = "Failed to create book";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates a book by ID.
     * @param id    ID of the book to update.
     * @param bookDTO    BookDTO containing the book information to update.
     * @return ResponseEntity containing a BookDTO and an HTTP status code.
     */
    @PutMapping("/books/{id}")
    public ResponseEntity<?> updateBook(@PathVariable("id") long id, @RequestBody BookDTO bookDTO) {
        try {
            BookDTO updatedBookDTO = bookService.updateBook(id, bookDTO);
            if (updatedBookDTO != null) {
                return new ResponseEntity<>(updatedBookDTO, HttpStatus.OK);
            } else {
                String errorMessage = "Book with id: " + id + " not found";
                return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            String errorMessage = "Failed to update book with id: " + id;
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a book by ID.
     * @param id    ID of the book to delete.
     * @return ResponseEntity containing a message and an HTTP status code.
     */
    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> destroyBook(@PathVariable("id") long id) {
        try {
            boolean isDeleted = bookService.deleteBook(id);
            if (isDeleted) {
                String successMessage = "Book with id: " + id + " deleted successfully";
                return new ResponseEntity<>(successMessage, HttpStatus.OK);
            } else {
                String errorMessage = "Book with id: " + id + " not found";
                return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            String errorMessage = "Failed to delete book with id: " + id;
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
