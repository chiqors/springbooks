package me.chiqors.springbooks.service;

import me.chiqors.springbooks.dto.DetailTransactionDTO;
import me.chiqors.springbooks.model.DetailTransaction;
import me.chiqors.springbooks.model.Transaction;
import me.chiqors.springbooks.repository.DetailTransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetailTransactionService {
    private final DetailTransactionRepository detailTransactionRepository;

    public DetailTransactionService(DetailTransactionRepository detailTransactionRepository) {
        this.detailTransactionRepository = detailTransactionRepository;
    }

    public List<DetailTransactionDTO> getDetailTransactionsByTransaction(Transaction transaction) {
        List<DetailTransaction> detailTransactions = detailTransactionRepository.findByTransaction(transaction);

        // Map DetailTransaction entities to DetailTransactionDTO objects

        return detailTransactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DetailTransactionDTO convertToDTO(DetailTransaction detailTransaction) {
        DetailTransactionDTO dto = new DetailTransactionDTO();
        dto.setBookId(detailTransaction.getBook().getId());
        dto.setTotal(detailTransaction.getTotal());

        return dto;
    }
}
