package me.chiqors.springbooks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    // private long id;
    private String title;
    private String author;
    private int stock;
    private String publishedAt;
    private String registeredAt;
    private boolean deleted;
    private String bookCode;
}