package me.chiqors.springbooks.repository;

import me.chiqors.springbooks.dto.DetailTransactionDTO;
import me.chiqors.springbooks.model.DetailTransaction;
import me.chiqors.springbooks.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailTransactionRepository extends JpaRepository<DetailTransaction, Long> {
    List<DetailTransaction> getDetailTransactionByTransactionId(long transactionId);
    List<DetailTransaction> findByTransaction(Transaction transaction);
}
