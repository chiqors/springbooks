package me.chiqors.springbooks.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "stock")
    private int stock;

    @Column(name = "published_at")
    private LocalDate publishedAt;

    @Column(name = "registered_at")
    private LocalDate registeredAt;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "book_code")
    private String bookCode;

    // -------------- Out Relationships --------------

    @OneToMany(mappedBy = "book")
    private List<DetailTransaction> detailTransactions;

    // -------------- Methods --------------

    // Default constructor is required by JPA
    public Book() {}

    @Override
    public String toString() {
        String content = "Book [";
        content += "id=" + id + ", ";
        content += "title=" + title + ", ";
        content += "author=" + author + ", ";
        content += "stock=" + stock + ", ";
        content += "publishedAt=" + publishedAt + ", ";
        content += "registeredAt=" + registeredAt + ", ";
        content += "deleted=" + deleted + "]";
        return content;
    }
}
