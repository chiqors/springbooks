package me.chiqors.springbooks.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDTO {
    @JsonIgnore
    private long id;

    private String title;

    private String author;

    private Integer stock;

    @JsonProperty("published_at")
    private String publishedAt;

    @JsonProperty("registered_at")
    private String registeredAt;

    @JsonIgnore
    private Boolean deleted;

    @JsonProperty("book_code")
    private String bookCode;

    @JsonProperty("updated_at")
    private String updatedAt;

    // @JsonIgnore can't be used together with @JsonProperty
    @JsonIgnore
    private String deletedAt;

    public BookDTO(String title, String author, int stock, String publishedAt, String registeredAt, String bookCode, String updatedAt) {
        this.title = title;
        this.author = author;
        this.stock = stock;
        this.publishedAt = publishedAt;
        this.registeredAt = registeredAt;
        this.bookCode = bookCode;
        this.updatedAt = updatedAt;
    }
}