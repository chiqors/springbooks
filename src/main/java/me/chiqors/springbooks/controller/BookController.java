package me.chiqors.springbooks.controller;

import java.util.List;

import me.chiqors.springbooks.config.Constant;
import me.chiqors.springbooks.dto.BookDTO;
import me.chiqors.springbooks.service.BookService;
import me.chiqors.springbooks.service.LogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping(Constant.API_PREFIX)
public class BookController {
    private final BookService bookService;
    private final LogService logService;

    @Autowired
    public BookController(BookService bookService, LogService logService) {
        this.bookService = bookService;
        this.logService = logService;
    }

    /**
     * Retrieves all books based on optional filtering, sorting, and pagination parameters.
     * @param title    Optional parameter to filter books by title.
     * @return ResponseEntity containing a list of BookDTOs and an HTTP status code.
     */
    @GetMapping("/books")
    public ResponseEntity<?> getAllBooks(@RequestParam(required = false) String title) {
        try {
            List<BookDTO> bookDTOs = bookService.getAllBooks(title);
            return new ResponseEntity<>(bookDTOs, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = "Failed to retrieve books";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a book by Code.
     * @param bookCode    Code of the book to retrieve.
     * @return ResponseEntity containing a BookDTO and an HTTP status code.
     */
    @GetMapping("/book/{code}")
    public ResponseEntity<?> getBookByCode(@PathVariable("code") String bookCode) {
        try {
            BookDTO bookDTO = bookService.getBookByCode(bookCode);
            if (bookDTO != null) {
                return new ResponseEntity<>(bookDTO, HttpStatus.OK);
            } else {
                String errorMessage = "Book with code: " + bookCode + " not found";
                return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            String errorMessage = "Failed to retrieve book with code: " + bookCode;
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
            logService.saveLog(Constant.API_PREFIX + "/books", Constant.HOST, "POST", HttpStatus.CREATED.value(), "Book created");
            return new ResponseEntity<>(createdBookDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            String errorMessage = "Failed to create book";
            logService.saveLog(Constant.API_PREFIX + "/books", Constant.HOST, "POST", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to create book");
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates a book by Code.
     * @param bookCode   Code of the book to update.
     * @param bookDTO    BookDTO containing the book information to update.
     * @return ResponseEntity containing a BookDTO and an HTTP status code.
     */
    @PutMapping("/books/{code}")
    public ResponseEntity<?> updateBook(@PathVariable("code") String bookCode, @RequestBody BookDTO bookDTO) {
        try {
            BookDTO updatedBookDTO = bookService.updateBook(bookCode, bookDTO);
            if (updatedBookDTO != null) {
                logService.saveLog(Constant.API_PREFIX + "/books/" + bookCode, Constant.HOST, "PUT", HttpStatus.OK.value(), "Book updated");
                return new ResponseEntity<>(updatedBookDTO, HttpStatus.OK);
            } else {
                String errorMessage = "Book with code: " + bookCode + " not found";
                logService.saveLog(Constant.API_PREFIX + "/books/" + bookCode, Constant.HOST, "PUT", HttpStatus.NOT_FOUND.value(), "Book not found");
                return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            String errorMessage = "Failed to update book with code: " + bookCode;
            logService.saveLog(Constant.API_PREFIX + "/books/" + bookCode, Constant.HOST, "PUT", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update book");
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a book by Code.
     * @param bookCode    Code of the book to delete.
     * @return ResponseEntity containing a message and an HTTP status code.
     */
    @DeleteMapping("/books/{code}")
    public ResponseEntity<?> destroyBook(@PathVariable("code") String bookCode) {
        try {
            boolean isDeleted = bookService.deleteBook(bookCode);
            if (isDeleted) {
                String successMessage = "Book with code: " + bookCode + " deleted successfully";
                logService.saveLog(Constant.API_PREFIX + "/books/" + bookCode, Constant.HOST, "DELETE", HttpStatus.OK.value(), "Book deleted");
                return new ResponseEntity<>(successMessage, HttpStatus.OK);
            } else {
                String errorMessage = "Book with code: " + bookCode + " not found";
                logService.saveLog(Constant.API_PREFIX + "/books/" + bookCode, Constant.HOST, "DELETE", HttpStatus.NOT_FOUND.value(), "Book not found");
                return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            String errorMessage = "Failed to delete book with code: " + bookCode;
            logService.saveLog(Constant.API_PREFIX + "/books/" + bookCode, Constant.HOST, "DELETE", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete book");
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
