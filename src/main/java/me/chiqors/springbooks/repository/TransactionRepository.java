package me.chiqors.springbooks.repository;

import me.chiqors.springbooks.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByBorrowedAtContaining(String date);
    List<Transaction> findAllByStatus(String status);
    List<Transaction> findAllByMemberId(long memberId);
}
