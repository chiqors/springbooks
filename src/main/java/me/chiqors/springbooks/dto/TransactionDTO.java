package me.chiqors.springbooks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private long id;
    private String transactionCode;
    private String borrowedAt;
    private String actReturnedAt;
    private String returnedAt;
    private long memberId;
    private String status;
    private int totalBooks;
    private String operatorName;
    private int totalFines;
    private List<DetailTransactionDTO> detailTransactions;
}
