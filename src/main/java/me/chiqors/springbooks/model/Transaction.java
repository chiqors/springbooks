package me.chiqors.springbooks.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "transaction_code")
    private String transactionCode;

    @Column(name = "borrowed_at")
    private String borrowedAt;

    @Column(name = "act_returned_at")
    private String actReturnedAt;

    @Column(name = "returned_at")
    private String returnedAt;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "member_name")
    private String memberName;

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
        content += "memberName=" + memberName + ", ";
        content += "status=" + status + ", ";
        content += "totalBooks=" + totalBooks + ", ";
        content += "operatorName=" + operatorName + ", ";
        content += "totalFines=" + totalFines + "]";
        return content;
    }
}
