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
