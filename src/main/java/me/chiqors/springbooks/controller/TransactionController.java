package me.chiqors.springbooks.controller;

import me.chiqors.springbooks.config.Constant;
import me.chiqors.springbooks.dto.TransactionDTO;
import me.chiqors.springbooks.service.LogService;
import me.chiqors.springbooks.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class TransactionController {
    private final TransactionService transactionService;
    private final LogService logService;

    @Autowired
    public TransactionController(TransactionService transactionService, LogService logService) {
        this.transactionService = transactionService;
        this.logService = logService;
    }

    /**
     * Retrieves all transactions based on optional filtering, sorting, and pagination parameters.
     *
     * @param date    Optional parameter to filter transactions by date.
     * @param memberId    Optional parameter to filter transactions by member ID.
     * @param sort    Optional parameter to specify the sorting order (e.g., "asc" or "desc").
     * @param page    Optional parameter to specify the page number for pagination.
     * @param size    Optional parameter to specify the page size for pagination.
     * @return ResponseEntity containing a list of TransactionDTOs and an HTTP status code.
     */
    @GetMapping("/transactions")
    public ResponseEntity<?> getAllTransactions(@RequestParam(required = false) String date,
                                                @RequestParam(required = false) Long memberId,
                                                @RequestParam(required = false) String sort,
                                                @RequestParam(required = false) Integer page,
                                                @RequestParam(required = false) Integer size) {
        try {
            List<TransactionDTO> transactionDTOs = transactionService.getAllTransactions(date, memberId, sort, page, size);
            return new ResponseEntity<>(transactionDTOs, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Failed to retrieve transactions";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a transaction by Code.
     *
     * @param transactionCode   ID of the transaction to retrieve.
     * @return ResponseEntity containing a TransactionDTO and an HTTP status code.
     */
    @GetMapping("/transaction/{code}")
    public ResponseEntity<?> getTransactionByCode(@PathVariable("code") String transactionCode) {
        try {
            TransactionDTO transactionDTO = transactionService.getTransactionByCode(transactionCode);
            if (transactionDTO != null) {
                return new ResponseEntity<>(transactionDTO, HttpStatus.OK);
            } else {
                String errorMessage = "Transaction with code: " + transactionCode + " not found";
                return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Failed to retrieve transaction";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a new transaction.
     *
     * @param transactionDTO    TransactionDTO containing the transaction data.
     * @return ResponseEntity containing the created TransactionDTO and an HTTP status code.
     */
    @PostMapping("/transactions")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        try {
            TransactionDTO newTransactionDTO = transactionService.addTransaction(transactionDTO);
            logService.saveLog(Constant.API_PREFIX + "/transactions", Constant.HOST, "POST", HttpStatus.CREATED.value(), "Created transaction with code: " + newTransactionDTO.getTransactionCode());
            return new ResponseEntity<>(newTransactionDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            String errorMessage = "Failed to create transaction";
            logService.saveLog(Constant.API_PREFIX + "/transactions", Constant.HOST, "POST", HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates a transaction.
     *
     * @param transactionCode    Code of the transaction to update.
     * @return ResponseEntity containing the updated TransactionDTO and an HTTP status code.
     */
    @PutMapping("/transactions/{code}")
    public ResponseEntity<?> updateTransaction(@PathVariable("code") String transactionCode) {
        try {
            TransactionDTO updatedTransactionDTO = transactionService.updateTransaction(transactionCode);
            if (updatedTransactionDTO != null) {
                logService.saveLog(Constant.API_PREFIX + "/transactions/" + transactionCode, Constant.HOST, "PUT", HttpStatus.OK.value(), "Updated transaction with code: " + transactionCode);
                return new ResponseEntity<>(updatedTransactionDTO, HttpStatus.OK);
            } else {
                String errorMessage = "Transaction with code: " + transactionCode + " not found";
                logService.saveLog(Constant.API_PREFIX + "/transactions/" + transactionCode, Constant.HOST, "PUT", HttpStatus.NOT_FOUND.value(), errorMessage);
                return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            String errorMessage = "Failed to update transaction";
            logService.saveLog(Constant.API_PREFIX + "/transactions/" + transactionCode, Constant.HOST, "PUT", HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
