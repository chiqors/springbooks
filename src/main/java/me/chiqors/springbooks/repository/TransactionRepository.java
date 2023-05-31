package me.chiqors.springbooks.repository;

import me.chiqors.springbooks.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByTransactionCode(String transactionCode);

    Page<Transaction> findAllByBorrowedAtContainingAndMemberIdOrderByBorrowedAtDesc(Date borrowedAt, long memberId, Pageable pageable);

    Page<Transaction> findAllByBorrowedAtContainingOrderByBorrowedAtDesc(Date borrowedAt, Pageable pageable);

    Page<Transaction> findAllByMemberIdOrderByBorrowedAtDesc(long memberId, Pageable pageable);

    Page<Transaction> findAllByOrderByBorrowedAtDesc(Pageable pageable);
}
