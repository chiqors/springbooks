package me.chiqors.springbooks.repository;

import me.chiqors.springbooks.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByBorrowedAtContaining(String date);
    List<Transaction> findAllByStatus(String status);
    List<Transaction> findAllByMemberId(long memberId);

    Transaction findByTransactionCode(String transactionCode);
}
