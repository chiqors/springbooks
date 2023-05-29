package me.chiqors.springbooks.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter @Setter
@Entity
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "transaction_code")
    private String transactionCode;

    @Column(name = "borrowed_at")
    private LocalDate borrowedAt;

    @Column(name = "act_returned_at")
    private LocalDate actReturnedAt;

    @Column(name = "returned_at")
    private LocalDate returnedAt;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "status")
    private String status;

    @Column(name = "total_books")
    private int totalBooks;

    @Column(name = "operator_name")
    private String operatorName;

    @Column(name = "total_fines")
    private int totalFines;

    // -------------- Out Relationships --------------

    @OneToMany(mappedBy = "transaction")
    private List<DetailTransaction> detailTransactions;

    // -------------- Methods --------------

    public Transaction(long transactionId) {
        this.id = transactionId;
    }

    public String getBorrowedAtString() {
        return borrowedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getActReturnedAtString() {
        return actReturnedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getReturnedAtString() {
        if (returnedAt == null) {
            return "";
        }
        return returnedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public String toString() {
        String memberContent = "Member [";
        memberContent += "id=" + member.getId() + ", ";
        memberContent += "name=" + member.getName() + ", ";
        memberContent += "email=" + member.getEmail() + ", ";
        memberContent += "phone=" + member.getPhone() + ", ";
        memberContent += "registeredAt=" + member.getRegisteredAt() + "]";

        String content = "Transaction [";
        content += "id=" + id + ", ";
        content += "transactionCode=" + transactionCode + ", ";
        content += "borrowedAt=" + borrowedAt + ", ";
        content += "actReturnedAt=" + actReturnedAt + ", ";
        content += "returnedAt=" + returnedAt + ", ";
        content += "member=" + memberContent + ", ";
        content += "status=" + status + ", ";
        content += "totalBooks=" + totalBooks + ", ";
        content += "operatorName=" + operatorName + ", ";
        content += "totalFines=" + totalFines + "]";
        return content;
    }
}
