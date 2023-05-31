package me.chiqors.springbooks.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.util.Date;
import java.util.List;

/**
 * Represents a transaction entity.
 */
@Getter @Setter
@Entity
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_code")
    private String transactionCode;

    @Column(name = "borrowed_at")
    private Date borrowedAt;

    @Column(name = "est_returned_at")
    private Date estReturnedAt;

    @Column(name = "returned_at")
    private Date returnedAt;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "status")
    private String status;

    @Column(name = "total_books")
    private Integer totalBooks;

    @Column(name = "operator_name")
    private String operatorName;

    @Column(name = "total_fines")
    private Integer totalFines;

    @Column(name = "updated_at")
    private Date updatedAt;

    // -------------- Out Relationships --------------

    @OneToMany(mappedBy = "transaction")
    private List<DetailTransaction> detailTransactions;

    // -------------- Methods --------------

    public Transaction(String transactionCode, Date borrowedAt, Date estReturnedAt, Date returnedAt, Member member, String status, int totalBooks, String operatorName, int totalFines, Date updatedAt) {
        this.transactionCode = transactionCode;
        this.borrowedAt = borrowedAt;
        this.estReturnedAt = estReturnedAt;
        this.returnedAt = returnedAt;
        this.member = member;
        this.status = status;
        this.totalBooks = totalBooks;
        this.operatorName = operatorName;
        this.totalFines = totalFines;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", transactionCode='" + transactionCode + '\'' +
                ", borrowedAt=" + borrowedAt +
                ", estReturnedAt=" + estReturnedAt +
                ", returnedAt=" + returnedAt +
                ", member=" + member +
                ", status='" + status + '\'' +
                ", totalBooks=" + totalBooks +
                ", operatorName='" + operatorName + '\'' +
                ", totalFines=" + totalFines +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
