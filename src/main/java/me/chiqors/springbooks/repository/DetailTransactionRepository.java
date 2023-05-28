package me.chiqors.springbooks.repository;

import me.chiqors.springbooks.dto.DetailTransactionDTO;
import me.chiqors.springbooks.model.DetailTransaction;
import me.chiqors.springbooks.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetailTransactionRepository extends JpaRepository<DetailTransaction, Long> {
    // get detail transaction by transaction id
    List<DetailTransaction> getDetailTransactionByTransactionId(long transactionId);

    List<DetailTransaction> findByTransaction(Transaction transaction);
}
