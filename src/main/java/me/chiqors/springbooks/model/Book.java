package me.chiqors.springbooks.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
    private String publishedAt;

    @Column(name = "registered_at")
    private String registeredAt;

    @Column(name = "deleted")
    private boolean deleted;

    // -------------- Out Relationships --------------

    @OneToMany(mappedBy = "book")
    private List<DetailTransaction> detailTransactions;

    // -------------- Methods --------------

    // Default constructor is required by JPA
    public Book() {}

    public Book(String title, String author, int stock, String publishedAt, String registeredAt) {
        this.title = title;
        this.author = author;
        this.stock = stock;
        this.publishedAt = publishedAt;
        this.registeredAt = registeredAt;
    }

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
