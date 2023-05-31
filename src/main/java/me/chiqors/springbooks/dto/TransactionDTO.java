package me.chiqors.springbooks.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TransactionDTO {
    @JsonIgnore
    private Long id;

    @JsonProperty("transaction_code")
    private String transactionCode;

    @JsonProperty("borrowed_at")
    private String borrowedAt;

    @JsonProperty("est_returned_at")
    private String estReturnedAt;

    @JsonProperty("returned_at")
    private String returnedAt;

    @JsonIgnore
    private Long memberId;

    private String status;

    @JsonProperty("total_books")
    private Integer totalBooks;

    @JsonProperty("operator_name")
    private String operatorName;

    @JsonProperty("total_fines")
    private Integer totalFines;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("detail_transactions")
    private List<DetailTransactionDTO> detailTransactions;

    @JsonProperty("member")
    private MemberDTO member;

    public TransactionDTO(String transactionCode, String borrowedAt, String estReturnedAt, String returnedAt, String status, Integer totalBooks, String operatorName, Integer totalFines, String updatedAt, List<DetailTransactionDTO> detailTransactions, MemberDTO memberDTO) {
        this.transactionCode = transactionCode;
        this.borrowedAt = borrowedAt;
        this.estReturnedAt = estReturnedAt;
        this.returnedAt = returnedAt;
        this.status = status;
        this.totalBooks = totalBooks;
        this.operatorName = operatorName;
        this.totalFines = totalFines;
        this.updatedAt = updatedAt;
        this.detailTransactions = detailTransactions;
        this.member = memberDTO;
    }
}
