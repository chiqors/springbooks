package me.chiqors.springbooks.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DetailTransactionDTO {
    @JsonIgnore
    private Long id;

    @JsonIgnore
    private Long transactionId;

    @JsonIgnore
    private Long bookId;

    private Integer total;

    @JsonProperty("book_code")
    private String bookCode;

    // include book dto
    private BookDTO book;

    public DetailTransactionDTO(Integer total, String bookCode, BookDTO bookDTO) {
        this.total = total;
        this.bookCode = bookCode;
        this.book = bookDTO;
    }
}
