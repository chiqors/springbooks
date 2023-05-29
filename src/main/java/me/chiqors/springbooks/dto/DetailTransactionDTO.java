package me.chiqors.springbooks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailTransactionDTO {
    private long id;
    private long transactionId;
    private long bookId;
    private int total;
}
