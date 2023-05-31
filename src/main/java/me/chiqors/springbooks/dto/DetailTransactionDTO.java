package me.chiqors.springbooks.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    // include book dto
    private BookDTO book;

    public DetailTransactionDTO(Integer total, BookDTO bookDTO) {
        this.total = total;
        this.book = bookDTO;
    }
}
