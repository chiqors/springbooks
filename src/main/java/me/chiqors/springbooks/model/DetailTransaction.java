package me.chiqors.springbooks.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Represents a detail transaction entity.
 */
@Getter @Setter
@Entity
@NoArgsConstructor
@Table(name = "detail_transactions")
public class DetailTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "total")
    private Integer total;

    // -------------- Methods --------------

    public DetailTransaction(Transaction transaction, Book book, Integer total) {
        this.transaction = transaction;
        this.book = book;
        this.total = total;
    }
}