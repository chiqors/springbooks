package me.chiqors.springbooks.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@Entity
@Table(name = "detail_transactions")
public class DetailTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "total")
    private int total;

    @Column(name = "snapshot")
    private String snapshot;

    // -------------- Methods --------------

    @Override
    public String toString() {
        String content = "DetailTransaction [";
        content += "id=" + id;
        content += ", transaction=" + transaction;
        content += ", book=" + book;
        content += ", total=" + total;
        content += ", snapshot=" + snapshot;
        return content;
    }
}