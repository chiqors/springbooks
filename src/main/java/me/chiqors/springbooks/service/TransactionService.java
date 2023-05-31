package me.chiqors.springbooks.service;

import me.chiqors.springbooks.dto.BookDTO;
import me.chiqors.springbooks.dto.DetailTransactionDTO;
import me.chiqors.springbooks.dto.MemberDTO;
import me.chiqors.springbooks.dto.TransactionDTO;
import me.chiqors.springbooks.model.Book;
import me.chiqors.springbooks.model.DetailTransaction;
import me.chiqors.springbooks.model.Member;
import me.chiqors.springbooks.model.Transaction;
import me.chiqors.springbooks.repository.BookRepository;
import me.chiqors.springbooks.repository.DetailTransactionRepository;
import me.chiqors.springbooks.repository.MemberRepository;
import me.chiqors.springbooks.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing transactions.
 */
@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private DetailTransactionRepository detailTransactionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BookRepository bookRepository;

    /**
     * Converts a Transaction entity to a TransactionDTO.
     *
     * @param transaction        the Transaction entity
     * @param detailTransactions the list of DetailTransactionDTOs
     * @return the TransactionDTO
     */
    public TransactionDTO convertToDTO(Transaction transaction, List<DetailTransactionDTO> detailTransactions) {
        String borrowedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transaction.getBorrowedAt());
        String actReturnedAt = new SimpleDateFormat("yyyy-MM-dd").format(transaction.getEstReturnedAt());
        String returnedAt = transaction.getReturnedAt() == null ? null : new SimpleDateFormat("yyyy-MM-dd").format(transaction.getReturnedAt());
        String updatedAt = transaction.getUpdatedAt() == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transaction.getUpdatedAt());

        MemberDTO memberDTO = convertMemberToDTO(transaction.getMember());

        return new TransactionDTO(
                transaction.getTransactionCode(),
                borrowedAt,
                actReturnedAt,
                returnedAt,
                transaction.getStatus(),
                transaction.getTotalBooks(),
                transaction.getOperatorName(),
                transaction.getTotalFines(),
                updatedAt,
                detailTransactions,
                memberDTO
        );
    }

    /**
     * Converts a DetailTransaction entity to a DetailTransactionDTO.
     *
     * @param detailTransaction the DetailTransaction entity
     * @return the DetailTransactionDTO
     */
    private DetailTransactionDTO convertDetailTransactionToDTO(DetailTransaction detailTransaction) {
        BookDTO bookDTO = convertBookToDTO(detailTransaction.getBook());

        return new DetailTransactionDTO(
                detailTransaction.getTotal(),
                bookDTO
        );
    }

    private BookDTO convertBookToDTO(Book book) {
        String publishedAt = new SimpleDateFormat("yyyy-MM-dd").format(book.getPublishedAt());
        String registeredAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(book.getRegisteredAt());
        String updatedAt = book.getUpdatedAt() == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(book.getUpdatedAt());

        return new BookDTO(
                book.getTitle(),
                book.getAuthor(),
                book.getStock(),
                publishedAt,
                registeredAt,
                book.getBookCode(),
                updatedAt
        );
    }

    private MemberDTO convertMemberToDTO(Member member) {
        String registeredAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(member.getRegisteredAt());
        String updatedAt = member.getUpdatedAt() == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(member.getUpdatedAt());

        return new MemberDTO(
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                registeredAt,
                member.getMemberCode(),
                updatedAt
        );
    }

    /**
     * Retrieves all transactions with optional filtering, sorting, and pagination.
     *
     * @param borrowedAt     the borrowed date filter (optional)
     * @param memberCode     the member Code filter (optional)
     * @param page           the page number (optional)
     * @param size           the page size (optional)
     * @return the paginated list of transactions
     */
    public Page<TransactionDTO> getAllTransactions(String borrowedAt, String memberCode, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Transaction> transactionPage;
        Member member;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (borrowedAt != null && memberCode != null) {
                Date borrowedAtDate = dateFormat.parse(borrowedAt);
                member = memberRepository.findByMemberCodeAndDeletedIsFalse(memberCode);
                transactionPage = transactionRepository.findAllByBorrowedAtContainingAndMemberIdOrderByBorrowedAtDesc(borrowedAtDate, member.getId(), pageable);
            } else if (borrowedAt != null) {
                Date borrowedAtDate = dateFormat.parse(borrowedAt);
                transactionPage = transactionRepository.findAllByBorrowedAtContainingOrderByBorrowedAtDesc(borrowedAtDate, pageable);
            } else if (memberCode != null) {
                member = memberRepository.findByMemberCodeAndDeletedIsFalse(memberCode);
                transactionPage = transactionRepository.findAllByMemberIdOrderByBorrowedAtDesc(member.getId(), pageable);
            } else {
                transactionPage = transactionRepository.findAllByOrderByBorrowedAtDesc(pageable);
            }

            // no need to add data for detailTransactions, just add array empty
            List<TransactionDTO> transactionDTOs = transactionPage.getContent().stream()
                    .map(transaction -> convertToDTO(transaction, new ArrayList<>()))
                    .collect(Collectors.toList());

            // add member: { name } to transactionDTO
            for (int i = 0; i < transactionDTOs.size(); i++) {
                MemberDTO memberDTO = convertMemberToDTO(transactionPage.getContent().get(i).getMember());
                // remove unnecessary data except name and memberCode
                memberDTO.setEmail(null);
                memberDTO.setPhone(null);
                memberDTO.setRegisteredAt(null);
                memberDTO.setUpdatedAt(null);
                transactionDTOs.get(i).setMember(memberDTO);
            }

            return new PageImpl<>(transactionDTOs, pageable, transactionPage.getTotalElements());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a transaction by its code.
     *
     * @param transactionCode the transaction code
     * @return the TransactionDTO
     */
    public TransactionDTO getTransactionByCode(String transactionCode) {
        Transaction transaction = transactionRepository.findByTransactionCode(transactionCode);
        if (transaction == null) {
            return null;
        } else {
            List<DetailTransactionDTO> detailTransactionDTOs = transaction.getDetailTransactions().stream()
                    .map(this::convertDetailTransactionToDTO)
                    .collect(Collectors.toList());
            return convertToDTO(transaction, detailTransactionDTOs);
        }
    }

    public boolean isValidTransactionCode(String transactionCode) {
        Transaction transaction = transactionRepository.findByTransactionCode(transactionCode);
        return transaction != null;
    }

    /**
     * Adds a new transaction.
     *
     * @param dto the TransactionDTO
     * @return the created TransactionDTO
     */
    @Transactional
    public TransactionDTO addTransaction(TransactionDTO dto) {
        // Generate transaction code. Format: T<day><month><year><hour><minute><second><memberCode>
        Date currentDate = new Date();
        String borrowedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentDate);
        String transactionCode = "T" + new SimpleDateFormat("ddMMyyyyHHmmss").format(currentDate) + dto.getMember().getMemberCode();
        dto.setTransactionCode(transactionCode);
        dto.setBorrowedAt(borrowedAt);
        dto.setStatus("borrowed");
        dto.setTotalFines(0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date estReturnedAt = dateFormat.parse(dto.getEstReturnedAt());

            // get member object from memberRepository (with memberCode)
            Member member = memberRepository.findByMemberCodeAndDeletedIsFalse(dto.getMember().getMemberCode());

            // get Total Books from detailTransactionsDTO.total for each detailTransactionDTO
            int totalBooks = 0;
            for (DetailTransactionDTO detailTransactionDTO : dto.getDetailTransactions()) {
                totalBooks += detailTransactionDTO.getTotal();
            }
            dto.setTotalBooks(totalBooks);

            Transaction transaction = new Transaction(
                    dto.getTransactionCode(),
                    currentDate,
                    estReturnedAt,
                    null,
                    member,
                    dto.getStatus(),
                    totalBooks,
                    dto.getOperatorName(),
                    dto.getTotalFines(),
                    null
            );

            transactionRepository.save(transaction);

            List<DetailTransactionDTO> detailTransactions = dto.getDetailTransactions();

            for (DetailTransactionDTO detailTransactionDTO : detailTransactions) {
                // get book object from bookRepository (with bookCode)
                Book book = bookRepository.findByBookCodeAndDeletedIsFalse(detailTransactionDTO.getBook().getBookCode());
                BookDTO bookDTO = convertBookToDTO(book);

                DetailTransaction detailTransaction = new DetailTransaction(
                        transaction,
                        book,
                        detailTransactionDTO.getTotal()
                );

                detailTransactionRepository.save(detailTransaction);

                // set book DTO for detailTransactionDTO
                detailTransactionDTO.setBook(bookDTO);

                // update book stock
                book.setStock(book.getStock() - detailTransactionDTO.getTotal());
                bookRepository.save(book);
            }

            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates a transaction by its code.
     *
     * @param transactionDTO the TransactionDTO
     * @return the updated TransactionDTO
     */
    @Transactional
    public TransactionDTO updateTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = transactionRepository.findByTransactionCode(transactionDTO.getTransactionCode());

        if (transaction.getStatus().equals("borrowed")) {
            transaction.setStatus("returned");
            transaction.setReturnedAt(new Date());
            transaction.setUpdatedAt(new Date());

            // calculate total fines
            if (transaction.getEstReturnedAt().compareTo(transaction.getReturnedAt()) < 0) {
                long diff = transaction.getReturnedAt().getTime() - transaction.getEstReturnedAt().getTime();
                long diffDays = diff / (24 * 60 * 60 * 1000);
                transaction.setTotalFines((int) diffDays * 1000); // Rp. 1.000 per day
            } else {
                transaction.setTotalFines(0);
            }

            transactionRepository.save(transaction);

            List<DetailTransaction> detailTransactions = transaction.getDetailTransactions();

            for (DetailTransaction detailTransaction : detailTransactions) {
                // get book object from bookRepository (with bookCode)
                Book book = bookRepository.findByBookCodeAndDeletedIsFalse(detailTransaction.getBook().getBookCode());

                // update book stock
                book.setStock(book.getStock() + detailTransaction.getTotal());
                bookRepository.save(book);
            }

            return convertToDTO(transaction, null);
        } else {
            return null;
        }
    }
}