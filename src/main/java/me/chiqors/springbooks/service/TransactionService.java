package me.chiqors.springbooks.service;

import me.chiqors.springbooks.dto.DetailTransactionDTO;
import me.chiqors.springbooks.dto.MemberDTO;
import me.chiqors.springbooks.dto.TransactionDTO;
import me.chiqors.springbooks.model.Book;
import me.chiqors.springbooks.model.DetailTransaction;
import me.chiqors.springbooks.model.Member;
import me.chiqors.springbooks.model.Transaction;

import me.chiqors.springbooks.repository.DetailTransactionRepository;
import me.chiqors.springbooks.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final DetailTransactionRepository detailTransactionRepository; // not the service, since it will be having infinite loop
    private final MemberService memberService;
    private final BookService bookService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, DetailTransactionRepository detailTransactionRepository, MemberService memberService, BookService bookService) {
        this.transactionRepository = transactionRepository;
        this.detailTransactionRepository = detailTransactionRepository;
        this.memberService = memberService;
        this.bookService = bookService;
    }

    public TransactionDTO convertToDTO(Transaction transaction, List<DetailTransactionDTO> detailTransactions) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setTransactionCode(transaction.getTransactionCode());
        dto.setBorrowedAt(transaction.getBorrowedAtString());
        dto.setActReturnedAt(transaction.getActReturnedAtString());
        dto.setReturnedAt(transaction.getReturnedAtString());
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

    private DetailTransactionDTO convertDetailTransactionToDTO(DetailTransaction detailTransaction) {
        DetailTransactionDTO dto = new DetailTransactionDTO();
        dto.setId(detailTransaction.getId());
        dto.setTransactionId(detailTransaction.getTransaction().getId());
        dto.setBookId(detailTransaction.getBook().getId());
        dto.setTotal(detailTransaction.getTotal());
        return dto;
    }

    public Transaction convertToEntity(TransactionDTO dto) {
        MemberDTO memberDTO = memberService.getMemberById(dto.getMemberId());
        Member member = memberService.convertToEntity(memberDTO);

        Transaction transaction = new Transaction();
        transaction.setId(dto.getId());
        transaction.setTransactionCode(dto.getTransactionCode());
        transaction.setBorrowedAt(LocalDate.parse(dto.getBorrowedAt()));
        transaction.setActReturnedAt(LocalDate.parse(dto.getActReturnedAt()));
        transaction.setReturnedAt(transaction.getReturnedAt() != null ? LocalDate.parse(dto.getReturnedAt()) : null);
        transaction.setMember(member);
        transaction.setStatus(dto.getStatus());
        transaction.setTotalBooks(dto.getTotalBooks());
        transaction.setOperatorName(dto.getOperatorName());
        transaction.setTotalFines(dto.getTotalFines());

        List<DetailTransaction> detailTransactions = dto.getDetailTransactions().stream()
                .map(detailTransactionDto -> {
                    DetailTransaction detailTransaction = new DetailTransaction();
                    detailTransaction.setTransaction(transaction);
                    detailTransaction.setBook(bookService.convertToEntity(bookService.getBookById(detailTransactionDto.getBookId())));
                    detailTransaction.setTotal(detailTransactionDto.getTotal());
                    return detailTransaction;
                })
                .collect(Collectors.toList());

        transaction.setDetailTransactions(detailTransactions);

        return transaction;
    }

    private DetailTransaction convertDetailTransactionToEntity(DetailTransactionDTO detailTransactionDTO, Transaction transaction) {
        DetailTransaction detailTransaction = new DetailTransaction();
        detailTransaction.setTransaction(transaction);
        detailTransaction.setBook(bookService.convertToEntity(bookService.getBookById(detailTransactionDTO.getBookId())));
        detailTransaction.setTotal(detailTransactionDTO.getTotal());
        return detailTransaction;
    }

    // ------------------- CRUD -------------------

    public List<TransactionDTO> getAllTransactions(String date, Long memberId, String sort, Integer page, Integer size) {
        List<Transaction> transactions = transactionRepository.findAll();

        if (date != null) {
            transactions = transactions.stream()
                    .filter(transaction -> transaction.getBorrowedAtString().equals(date))
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
            int end = Math.min(start + size, transactions.size());

            if (start > transactions.size()) {
                return null;
            }

            transactions = transactions.subList(start, end);
        }

        List<TransactionDTO> transactionDTOs = new ArrayList<>();
        List<DetailTransactionDTO> detailTransactionDTOs = new ArrayList<>();

        for (Transaction transaction : transactions) {
            TransactionDTO transactionDTO = convertToDTO(transaction, detailTransactionDTOs);
            transactionDTOs.add(transactionDTO);
        }

        return transactionDTOs;
    }

    public TransactionDTO getTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        List<DetailTransactionDTO> detailTransactions = transaction.getDetailTransactions().stream()
                .map(this::convertDetailTransactionToDTO)
                .collect(Collectors.toList());

        return convertToDTO(transaction, detailTransactions);
    }

    public TransactionDTO addTransaction(TransactionDTO dto) {
        // Generate transaction code. Format: T-<year><month><day>-S<hour><minute><second>-M<memberId>
        String transactionCode = "T-" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "-S" + LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss")) + "-M" + dto.getMemberId();
        dto.setTransactionCode(transactionCode);
        dto.setBorrowedAt(LocalDate.now().toString());
        dto.setStatus("borrowed");

        Transaction transaction = convertToEntity(dto);
        Transaction savedTransaction = transactionRepository.save(transaction);

        List<DetailTransaction> detailTransactions = dto.getDetailTransactions().stream()
                .map(detailTransactionDTO -> convertDetailTransactionToEntity(detailTransactionDTO, savedTransaction))
                .collect(Collectors.toList());

        detailTransactionRepository.saveAll(detailTransactions);

        return dto;
    }

    public TransactionDTO updateTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        transaction.setReturnedAt(LocalDate.now());
        transaction.setStatus("returned");

        List<DetailTransaction> detailTransactions = transaction.getDetailTransactions();

        for (DetailTransaction detailTransaction : detailTransactions) {
            Book book = detailTransaction.getBook();
            book.setStock(book.getStock() + detailTransaction.getTotal());
            bookService.updateBook(book.getId(), bookService.convertToDTO(book));
        }

        // get current date and check if it's past the actReturnedAt date
        LocalDate currentDate = LocalDate.now();
        LocalDate actReturnedAt = transaction.getActReturnedAt();

        if (currentDate.isAfter(actReturnedAt)) {
            // calculate the difference between the current date and the actReturnedAt date
            double daysBetween = ChronoUnit.DAYS.between(actReturnedAt, currentDate);
            double totalFines = daysBetween * 1000;
            int parsedTotalFines = Integer.parseInt(String.valueOf(totalFines));
            transaction.setTotalFines(parsedTotalFines);
        } else {
            transaction.setTotalFines(transaction.getTotalFines());
        }

        transactionRepository.save(transaction);

        return convertToDTO(transaction, detailTransactions.stream()
                .map(this::convertDetailTransactionToDTO)
                .collect(Collectors.toList()));
    }
}

