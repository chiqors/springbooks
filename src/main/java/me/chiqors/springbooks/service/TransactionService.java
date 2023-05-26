package me.chiqors.springbooks.service;

import me.chiqors.springbooks.dto.TransactionDTO;
import me.chiqors.springbooks.model.Member;
import me.chiqors.springbooks.model.Transaction;

import me.chiqors.springbooks.repository.MemberRepository;
import me.chiqors.springbooks.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, MemberRepository memberRepository) {
        this.transactionRepository = transactionRepository;
        this.memberRepository = memberRepository;
    }

    public TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setTransactionCode(transaction.getTransactionCode());
        transactionDTO.setBorrowedAt(transaction.getBorrowedAt());
        transactionDTO.setActReturnedAt(transaction.getActReturnedAt());
        transactionDTO.setReturnedAt(transaction.getReturnedAt());
        transactionDTO.setMemberId(transaction.getMember().getId());
        transactionDTO.setStatus(transaction.getStatus());
        transactionDTO.setTotalBooks(transaction.getDetailTransactions().size());
        transactionDTO.setOperatorName(transaction.getOperatorName());
        transactionDTO.setTotalFines(transaction.getTotalFines());

        return transactionDTO;
    }

    public List<TransactionDTO> getAllTransactions(String date, Long memberId, String sort, Integer page, Integer size) {
        List<Transaction> transactions;
        if (date != null) {
            transactions = transactionRepository.findAllByBorrowedAtContaining(date);
        } else if (memberId != null) {
            transactions = transactionRepository.findAllByMemberId(memberId);
        } else {
            transactions = transactionRepository.findAll();
        }

        if (sort != null) {
            switch (sort) {
                case "borrowedAt":
                    transactions.sort(Comparator.comparing(Transaction::getBorrowedAt));
                    break;
                case "actReturnedAt":
                    transactions.sort(Comparator.comparing(Transaction::getActReturnedAt));
                    break;
                case "returnedAt":
                    transactions.sort(Comparator.comparing(Transaction::getReturnedAt));
                    break;
                case "memberId":
                    transactions.sort(Comparator.comparing(transaction -> transaction.getMember().getId()));
                    break;
                case "status":
                    transactions.sort(Comparator.comparing(Transaction::getStatus));
                    break;
                case "operatorName":
                    transactions.sort(Comparator.comparing(Transaction::getOperatorName));
                    break;
                case "totalFines":
                    transactions.sort(Comparator.comparing(Transaction::getTotalFines));
                    break;
                default:
                    break;
            }
        }

        if (page != null && size != null) {
            int start = (page - 1) * size;
            int end = Math.min(start + size, transactions.size());
            return transactions.subList(start, end).stream().map(this::convertToDTO).collect(Collectors.toList());
        }

        return transactions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public TransactionDTO getTransactionById(long id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction != null) {
            return convertToDTO(transaction);
        }
        return null;
    }

    public TransactionDTO addTransaction(TransactionDTO transactionDTO) {
        // Retrieve the Member using the member ID from the DTO
        Member member = memberRepository.findById(transactionDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        Transaction transaction = new Transaction();
        transaction.setTransactionCode(transactionDTO.getTransactionCode());
        transaction.setBorrowedAt(transactionDTO.getBorrowedAt());
        transaction.setActReturnedAt(transactionDTO.getActReturnedAt());
        transaction.setReturnedAt(transactionDTO.getReturnedAt());
        transaction.setMember(member);
        transaction.setStatus(transactionDTO.getStatus());
        transaction.setOperatorName(transactionDTO.getOperatorName());
        transaction.setTotalFines(transactionDTO.getTotalFines());

        transactionRepository.save(transaction);
        return convertToDTO(transaction);
    }

    public TransactionDTO updateTransaction(long id, TransactionDTO transactionDTO) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction != null) {
            transaction.setTransactionCode(transactionDTO.getTransactionCode());
            transaction.setBorrowedAt(transactionDTO.getBorrowedAt());
            transaction.setActReturnedAt(transactionDTO.getActReturnedAt());
            transaction.setReturnedAt(transactionDTO.getReturnedAt());
            transaction.setStatus(transactionDTO.getStatus());
            transaction.setOperatorName(transactionDTO.getOperatorName());
            transaction.setTotalFines(transactionDTO.getTotalFines());

            transactionRepository.save(transaction);
            return convertToDTO(transaction);
        }
        return null;
    }
}
