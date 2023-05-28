package me.chiqors.springbooks.service;

import me.chiqors.springbooks.dto.DetailTransactionDTO;
import me.chiqors.springbooks.dto.MemberDTO;
import me.chiqors.springbooks.dto.TransactionCreateDTO;
import me.chiqors.springbooks.dto.TransactionDTO;
import me.chiqors.springbooks.model.Member;
import me.chiqors.springbooks.model.Transaction;

import me.chiqors.springbooks.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final DetailTransactionService detailTransactionService;
    private final MemberService memberService;

    public TransactionService(TransactionRepository transactionRepository, DetailTransactionService detailTransactionService, MemberService memberService) {
        this.transactionRepository = transactionRepository;
        this.detailTransactionService = detailTransactionService;
        this.memberService = memberService;
    }

    private TransactionDTO convertToDTO(Transaction transaction, List<DetailTransactionDTO> detailTransactions) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setTransactionCode(transaction.getTransactionCode());
        dto.setBorrowedAt(transaction.getBorrowedAt());
        dto.setActReturnedAt(transaction.getActReturnedAt());
        dto.setReturnedAt(transaction.getReturnedAt());
        dto.setMemberId(transaction.getMember().getId());
        dto.setStatus(transaction.getStatus());
        dto.setTotalBooks(transaction.getTotalBooks());
        dto.setOperatorName(transaction.getOperatorName());
        dto.setTotalFines(transaction.getTotalFines());

        // Map detail transactions to DTOs if needed
        if (detailTransactions != null) {
            dto.setDetailTransactions(detailTransactions);
        }

        return dto;
    }

    private MemberDTO convertToMemberDTO(Member member) {
        return getMemberDTO(member);
    }

    static MemberDTO getMemberDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setName(member.getName());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setPhone(member.getPhone());
        memberDTO.setRegisteredAt(member.getRegisteredAt());

        return memberDTO;
    }

    public List<TransactionDTO> getAllTransactions(String date, Long memberId, String sort, Integer page, Integer size) {
        List<Transaction> transactions = transactionRepository.findAll();

        if (date != null) {
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getBorrowedAt().contains(date))
                    .collect(Collectors.toList());
        }

        if (memberId != null) {
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getMember().getId() == memberId)
                    .collect(Collectors.toList());
        }

        if (sort != null) {
            if (sort.equals("asc")) {
                transactions.sort(Comparator.comparing(Transaction::getBorrowedAt));
            } else if (sort.equals("desc")) {
                transactions.sort(Comparator.comparing(Transaction::getBorrowedAt).reversed());
            }
        }

        if (page != null && size != null) {
            int start = (page - 1) * size;
            int end = start + size;

            if (start > transactions.size()) {
                return null;
            }

            if (end > transactions.size()) {
                end = transactions.size();
            }

            transactions = transactions.subList(start, end);
        }

        return transactions.stream()
                .map(transaction -> convertToDTO(transaction, null))
                .collect(Collectors.toList());
    }

    public TransactionDTO getTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        List<DetailTransactionDTO> detailTransactions = detailTransactionService.getDetailTransactionsByTransaction(transaction);

        return convertToDTO(transaction, detailTransactions);
    }

//    public TransactionDTO createTransaction(TransactionCreateDTO createDTO) {
//        MemberDTO memberDTO = memberService.getMemberById(createDTO.getMemberId());
//
//        Transaction transaction = new Transaction();
//        transaction.setTransactionCode(createDTO.generateTransactionCode());
//        transaction.setMember();
//    }
}

