package me.chiqors.springbooks.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter @Setter
public class TransactionCreateDTO {
    private Long memberId;
    private String actReturnedAt;

    // -------------- Methods --------------

    public String generateTransactionCode() {
        // Transaction Code: T(DDMMYYYY)S(HHMMSS)M(MEMBER_ID)
        // Example: T01012020S120000M1
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        String transactionCode = "T";
        transactionCode += currentDate.format(DateTimeFormatter.ofPattern("ddMMuuuu"));
        transactionCode += "S";
        transactionCode += currentTime.format(DateTimeFormatter.ofPattern("HHmmss"));
        transactionCode += "M";
        transactionCode += this.memberId;
        return transactionCode;
    }
}

