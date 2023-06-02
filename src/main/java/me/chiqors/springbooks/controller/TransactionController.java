package me.chiqors.springbooks.controller;

import me.chiqors.springbooks.config.ApplicationProperties;
import me.chiqors.springbooks.dto.TransactionDTO;
import me.chiqors.springbooks.service.LogService;
import me.chiqors.springbooks.service.TransactionService;

import me.chiqors.springbooks.util.FormValidation;
import me.chiqors.springbooks.util.JSONResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("${api.prefix}") // cant use ApplicationProperties.API_PREFIX since it is static final
public class TransactionController {
    @Autowired
    private FormValidation formValidation;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private LogService logService;

    /**
     * Retrieves all transactions based on optional filtering, sorting, and pagination parameters.
     *
     * @param borrowedAt   Optional parameter to filter transactions by date.
     * @param memberCode   Optional parameter to filter transactions by member ID.
     * @param page         Optional parameter to specify the page number for pagination.
     * @param size         Optional parameter to specify the page size for pagination.
     * @return ResponseEntity containing a list of TransactionDTOs and an HTTP status code.
     */
    @GetMapping("/transactions")
    public ResponseEntity<JSONResponse> getAllTransactions(@RequestParam(required = false) String borrowedAt,
                                                           @RequestParam(required = false) String memberCode,
                                                           @RequestParam(required = false, defaultValue = "1") Integer page,
                                                           @RequestParam(required = false, defaultValue = "3") Integer size) {
        try {
            Page<TransactionDTO> transactionDTOList = transactionService.getAllTransactions(borrowedAt, memberCode, page, size);
            if (transactionDTOList != null) {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.OK.value(), "Transactions retrieved", transactionDTOList, null);
                return ResponseEntity.ok(jsonResponse);
            } else {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.NOT_FOUND.value(), "Transactions not found", null, null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JSONResponse jsonResponse = new JSONResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error", null, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
        }
    }

    /**
     * Retrieves a transaction by Code.
     *
     * @param transactionCode   ID of the transaction to retrieve.
     * @return ResponseEntity containing a TransactionDTO and an HTTP status code.
     */
    @GetMapping("/transaction/{code}")
    public ResponseEntity<JSONResponse> getTransactionByCode(@PathVariable("code") String transactionCode) {
        try {
            TransactionDTO transactionDTO = transactionService.getTransactionByCode(transactionCode);
            if (transactionDTO != null) {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.OK.value(), "Transaction retrieved", transactionDTO, null);
                return ResponseEntity.ok(jsonResponse);
            } else {
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.NOT_FOUND.value(), "Transaction not found", null, null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
            }
        } catch (Exception e) {
            JSONResponse jsonResponse = new JSONResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error", null, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
        }
    }

    /**
     * Creates a new transaction.
     *
     * @param transactionDTO    TransactionDTO containing the transaction data.
     * @return ResponseEntity containing the created TransactionDTO and an HTTP status code.
     */
    @PostMapping("/transactions")
    public ResponseEntity<JSONResponse> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        List<String> errors = formValidation.createTransactionValidation(transactionDTO);
        if (errors.isEmpty()) {
            try {
                TransactionDTO createdTransactionDTO = transactionService.addTransaction(transactionDTO);
                if (createdTransactionDTO != null) {
                    JSONResponse jsonResponse = new JSONResponse(HttpStatus.CREATED.value(), "Transaction created", createdTransactionDTO, null);
                    logService.saveLog(ApplicationProperties.API_PREFIX + "/transactions", ApplicationProperties.HOST, "POST", HttpStatus.CREATED.value(), "Transaction created");
                    return ResponseEntity.status(HttpStatus.CREATED).body(jsonResponse);
                } else {
                    JSONResponse jsonResponse = new JSONResponse(HttpStatus.BAD_REQUEST.value(), "Transaction not created", null, null);
                    logService.saveLog(ApplicationProperties.API_PREFIX + "/transactions", ApplicationProperties.HOST, "POST", HttpStatus.BAD_REQUEST.value(), "Transaction not created");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error", null, null);
                logService.saveLog(ApplicationProperties.API_PREFIX + "/transactions", ApplicationProperties.HOST, "POST", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
            }
        } else {
            JSONResponse jsonResponse = new JSONResponse(HttpStatus.BAD_REQUEST.value(), "Invalid form", null, errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
        }
    }

    /**
     * Updates a transaction.
     *
     * @param transactionDTO    TransactionDTO containing the transaction data.
     * @return ResponseEntity   containing the updated TransactionDTO and an HTTP status code.
     */
    @PutMapping("/transactions")
    public ResponseEntity<JSONResponse> updateTransaction(@RequestBody TransactionDTO transactionDTO) {
        List<String> errors = formValidation.updateTransactionValidation(transactionDTO);
        if (errors.isEmpty()) {
            try {
                TransactionDTO updatedTransactionDTO = transactionService.updateTransaction(transactionDTO);
                if (updatedTransactionDTO != null) {
                    JSONResponse jsonResponse = new JSONResponse(HttpStatus.OK.value(), "Transaction updated", updatedTransactionDTO, null);
                    logService.saveLog(ApplicationProperties.API_PREFIX + "/transactions", ApplicationProperties.HOST, "PUT", HttpStatus.OK.value(), "Transaction updated");
                    return ResponseEntity.ok(jsonResponse);
                } else {
                    JSONResponse jsonResponse = new JSONResponse(HttpStatus.BAD_REQUEST.value(), "Transaction not updated", null, null);
                    logService.saveLog(ApplicationProperties.API_PREFIX + "/transactions", ApplicationProperties.HOST, "PUT", HttpStatus.BAD_REQUEST.value(), "Transaction not updated");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JSONResponse jsonResponse = new JSONResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error", null, null);
                logService.saveLog(ApplicationProperties.API_PREFIX + "/transactions", ApplicationProperties.HOST, "PUT", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonResponse);
            }
        } else {
            JSONResponse jsonResponse = new JSONResponse(HttpStatus.BAD_REQUEST.value(), "Invalid form", null, errors);
            logService.saveLog(ApplicationProperties.API_PREFIX + "/transactions", ApplicationProperties.HOST, "PUT", HttpStatus.BAD_REQUEST.value(), "Invalid form");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
        }
    }
}
