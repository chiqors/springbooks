package me.chiqors.springbooks.controller;

import java.util.List;

import me.chiqors.springbooks.config.ApplicationProperties;
import me.chiqors.springbooks.dto.BookDTO;
import me.chiqors.springbooks.service.BookService;
import me.chiqors.springbooks.service.LogService;
import me.chiqors.springbooks.util.FormValidation;
import me.chiqors.springbooks.util.JSONResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("${api.prefix}") // cant use ApplicationProperties.API_PREFIX since it is static final
public class BookController {
    @Autowired
    private FormValidation formValidation;
    @Autowired
    private BookService bookService;
    @Autowired
    private LogService logService;

    /**
     * Retrieves all books based on optional filtering, sorting, and pagination parameters.
     * @param title    Optional parameter to filter books by title.
     * @param page     Optional parameter to specify the page number of the results.
     * @param size     Optional parameter to specify the number of results per page.
     * @return ResponseEntity containing a JSONResponse and an HTTP status code.
     */
    @GetMapping("/books")
    public ResponseEntity<JSONResponse> getAllBooks(@RequestParam(value = "title", required = false) String title,
                                                    @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                    @RequestParam(value = "size", required = false, defaultValue = "3") Integer size) {
        try {
            Page<BookDTO> bookDTOList = bookService.getAllBooks(title, page, size);
            if (bookDTOList != null) {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.OK.value(), "Books retrieved", bookDTOList, null);
                return ResponseEntity.ok(jsonResponse);
            } else {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.NOT_FOUND.value(), "Books not found", null, null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            JSONResponse jsonResponse = new JSONResponse(HttpStatus.BAD_REQUEST.value(), "Invalid page or size", null, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            JSONResponse jsonResponse = new JSONResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve books", null, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
        }
    }

    /**
     * Retrieves a book by Code.
     * @param bookCode    Code of the book to retrieve.
     * @return ResponseEntity containing a BookDTO and an HTTP status code.
     */
    @GetMapping("/book/{code}")
    public ResponseEntity<JSONResponse> getBookByCode(@PathVariable("code") String bookCode) {
        try {
            BookDTO bookDTO = bookService.getBookByCode(bookCode);
            if (bookDTO != null) {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.OK.value(), "Book retrieved", bookDTO, null);
                return ResponseEntity.ok(jsonResponse);
            } else {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.NOT_FOUND.value(), "Book not found", null, null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JSONResponse jsonResponse = new JSONResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve book", null, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
        }
    }

    /**
     * Creates a new book.
     * @param bookDTO    BookDTO containing the book information to create.
     * @return ResponseEntity containing a BookDTO and an HTTP status code.
     */
    @PostMapping("/books")
    public ResponseEntity<JSONResponse> createBook(@RequestBody BookDTO bookDTO) {
        List<String> errors = formValidation.createBookValidation(bookDTO);
        if (errors.isEmpty()) {
            BookDTO createdBookDTO = bookService.addBook(bookDTO);
            if (createdBookDTO != null) {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.CREATED.value(), "Book created", createdBookDTO, null);
                logService.saveLog(ApplicationProperties.API_PREFIX+ "/books", ApplicationProperties.HOST, "POST", HttpStatus.CREATED.value(), "Book created");
                return ResponseEntity.status(HttpStatus.CREATED).body(jsonResponse);
            } else {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to create book", null, null);
                logService.saveLog(ApplicationProperties.API_PREFIX+ "/books", ApplicationProperties.HOST, "POST", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to create book");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
            }
        } else {
            JSONResponse jsonResponse = new JSONResponse(HttpStatus.BAD_REQUEST.value(), "Failed to create book", null, errors);
            logService.saveLog(ApplicationProperties.API_PREFIX+ "/books", ApplicationProperties.HOST, "POST", HttpStatus.BAD_REQUEST.value(), "Failed to create book");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
        }
    }

    /**
     * Updates a book by Code.
     * @param bookDTO    BookDTO containing the book information to update.
     * @return ResponseEntity containing a BookDTO and an HTTP status code.
     */
    @PutMapping("/books")
    public ResponseEntity<JSONResponse> updateBook(@RequestBody BookDTO bookDTO) {
        List<String> errors = formValidation.updateBookValidation(bookDTO);
        if (errors.isEmpty()) {
            BookDTO updatedBookDTO = bookService.updateBook(bookDTO);
            if (updatedBookDTO != null) {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.OK.value(), "Book updated", updatedBookDTO, null);
                logService.saveLog(ApplicationProperties.API_PREFIX+ "/books/" + bookDTO.getBookCode(), ApplicationProperties.HOST, "PUT", HttpStatus.OK.value(), "Book updated");
                return ResponseEntity.ok(jsonResponse);
            } else {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update book", null, null);
                logService.saveLog(ApplicationProperties.API_PREFIX+ "/books/" + bookDTO.getBookCode(), ApplicationProperties.HOST, "PUT", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update book");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
            }
        } else {
            JSONResponse jsonResponse = new JSONResponse(HttpStatus.BAD_REQUEST.value(), "Failed to update book", null, errors);
            logService.saveLog(ApplicationProperties.API_PREFIX+ "/books/" + bookDTO.getBookCode(), ApplicationProperties.HOST, "PUT", HttpStatus.BAD_REQUEST.value(), "Failed to update book");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
        }
    }

    /**
     * Deletes a book by Code.
     * @param bookCode    Code of the book to delete.
     * @return ResponseEntity containing a message and an HTTP status code.
     */
    @DeleteMapping("/books/{code}")
    public ResponseEntity<JSONResponse> destroyBook(@PathVariable("code") String bookCode) {
        List<String> errors = formValidation.destroyBookValidation(bookCode);
        if (errors.isEmpty()) {
            boolean isDeleted = bookService.deleteBook(bookCode);
            if (isDeleted) {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.OK.value(), "Book deleted", null, null);
                logService.saveLog(ApplicationProperties.API_PREFIX+ "/books/" + bookCode, ApplicationProperties.HOST, "DELETE", HttpStatus.OK.value(), "Book deleted");
                return ResponseEntity.ok(jsonResponse);
            } else {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete book", null, null);
                logService.saveLog(ApplicationProperties.API_PREFIX+ "/books/" + bookCode, ApplicationProperties.HOST, "DELETE", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete book");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
            }
        } else {
            JSONResponse jsonResponse = new JSONResponse(HttpStatus.BAD_REQUEST.value(), "Failed to delete book", null, errors);
            logService.saveLog(ApplicationProperties.API_PREFIX+ "/books/" + bookCode, ApplicationProperties.HOST, "DELETE", HttpStatus.BAD_REQUEST.value(), "Failed to delete book");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
        }
    }
}
