package me.chiqors.springbooks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}