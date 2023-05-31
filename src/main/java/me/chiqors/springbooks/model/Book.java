package me.chiqors.springbooks.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.Date;
import java.util.List;

/**
 * Represents a book entity.
 */
@Getter @Setter
@Entity
@NoArgsConstructor
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
    private Date publishedAt;

    @Column(name = "registered_at")
    private Date registeredAt;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "book_code")
    private String bookCode;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    // -------------- Out Relationships --------------

    @OneToMany(mappedBy = "book")
    private List<DetailTransaction> detailTransactions;

    // -------------- Methods --------------

    public Book(String title, String author, int stock, Date publishedAt, Date registeredAt, String bookCode, Date updatedAt) {
        this.title = title;
        this.author = author;
        this.stock = stock;
        this.publishedAt = publishedAt;
        this.registeredAt = registeredAt;
        this.bookCode = bookCode;
        this.updatedAt = updatedAt;
    }
}