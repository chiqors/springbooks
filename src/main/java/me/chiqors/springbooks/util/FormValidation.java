/**
 * The FormValidation class provides utility methods for validating form inputs related to books, members, and transactions.
 * It includes validation methods for creating, updating, and destroying books, members, and transactions.
 */

package me.chiqors.springbooks.util;

import me.chiqors.springbooks.config.ApplicationProperties;
import me.chiqors.springbooks.dto.DetailTransactionDTO;
import me.chiqors.springbooks.dto.MemberDTO;
import me.chiqors.springbooks.dto.TransactionDTO;
import me.chiqors.springbooks.service.BookService;
import me.chiqors.springbooks.dto.BookDTO;
import me.chiqors.springbooks.service.MemberService;
import me.chiqors.springbooks.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FormValidation {
    @Autowired
    private BookService bookService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ApplicationProperties applicationProperties;

    /**
     * Validates the inputs for creating a book.
     *
     * @param bookDTO The BookDTO object containing the book information.
     * @return A list of validation errors, if any.
     */
    public List<String> createBookValidation(BookDTO bookDTO) {
        List<String> errors = new ArrayList<>();

        if (bookDTO.getTitle() == null) {
            errors.add("Title is required");
        }

        if (bookDTO.getAuthor() == null) {
            errors.add("Author is required");
        }

        if (bookDTO.getPublishedAt() == null) {
            errors.add("Published date is required");
        } else {
            if (!isValidDateFormat(bookDTO.getPublishedAt())) {
                errors.add("Invalid published date format. It should be yyyy-MM-dd");
            }
        }

        if (bookDTO.getStock() == null) {
            errors.add("Stock is required");
        } else {
            // check if stock is not empty, and not negative
            if (bookDTO.getStock() < 0) {
                errors.add("Stock cannot be negative");
            }
        }

        return errors;
    }

    /**
     * Validates the inputs for updating a book.
     *
     * @param bookDTO The BookDTO object containing the book information.
     * @return A list of validation errors, if any.
     */
    public List<String> updateBookValidation(BookDTO bookDTO) {
        List<String> errors = new ArrayList<>();

        if (bookDTO.getTitle() == null && bookDTO.getAuthor() == null && bookDTO.getPublishedAt() == null && bookDTO.getStock() == null) {
            errors.add("At least one field (title, author, published date, stock) is required for update");
        }

        if (bookDTO.getPublishedAt() != null) {
            if (!isValidDateFormat(bookDTO.getPublishedAt())) {
                errors.add("Invalid published date format. It should be yyyy-MM-dd");
            }
        }

        // check if stock is not empty, and not negative
        if (bookDTO.getStock() != null && bookDTO.getStock() < 0) {
            errors.add("Stock cannot be negative");
        }

        if (bookDTO.getBookCode() == null) {
            errors.add("Book code is required for update");
        } else {
            boolean isValidBookCode = bookService.isValidBookCode(bookDTO.getBookCode());
            if (!isValidBookCode) {
                errors.add("Invalid book code");
            }
        }

        return errors;
    }

    /**
     * Validates the input for destroying a book.
     *
     * @param bookCode The code of the book to be destroyed.
     * @return A list of validation

    errors, if any.
     */
    public List<String> destroyBookValidation(String bookCode) {
        List<String> errors = new ArrayList<>();

        if (bookCode == null) {
            errors.add("Book code is required");
        } else {
            boolean isValidBookCode = bookService.isValidBookCode(bookCode);
            if (!isValidBookCode) {
                errors.add("Invalid book code");
            }
        }

        return errors;
    }

    /**
     * Validates the inputs for creating a member.
     *
     * @param memberDTO The MemberDTO object containing the member information.
     * @return A list of validation errors, if any.
     */
    public List<String> createMemberValidation(MemberDTO memberDTO) {
        List<String> errors = new ArrayList<>();

        if (memberDTO.getName() == null) {
            errors.add("Name is required");
        }

        if (memberDTO.getEmail() == null) {
            errors.add("Email is required");
        } else {
            if (!memberDTO.getEmail().matches(".*@.*")) {
                errors.add("Invalid email format");
            }
        }

        if (memberDTO.getPhone() == null) {
            errors.add("Phone is required");
        } else {
            if (!memberDTO.getPhone().matches("\\d{15}")) {
                errors.add("Invalid phone format. It should be max 15 digits");
            }
        }

        return errors;
    }

    /**
     * Validates the inputs for updating a member.
     *
     * @param memberDTO The MemberDTO object containing the member information.
     * @return A list of validation errors, if any.
     */
    public List<String> updateMemberValidation(MemberDTO memberDTO) {
        List<String> errors = new ArrayList<>();

        if (memberDTO.getName() == null && memberDTO.getEmail() == null && memberDTO.getPhone() == null) {
            errors.add("At least one field (name, email, phone) is required for update");
        }

        if (memberDTO.getEmail() != null) {
            if (!memberDTO.getEmail().matches(".*@.*")) {
                errors.add("Invalid email format");
            }
        }

        if (memberDTO.getPhone() != null) {
            // format: 012345678901234, max 15 digits, min 8 digits
            if (!memberDTO.getPhone().matches("\\d{8,15}")) {
                errors.add("Invalid phone format. It should be between 8 and 15 digits.");
            }
        }

        if (memberDTO.getMemberCode() == null) {
            errors.add("Member code is required for update");
        } else {
            boolean isValidMemberCode = memberService.isValidMemberCode(memberDTO.getMemberCode());
            if (!isValidMemberCode) {
                errors.add("Invalid member code");
            }
        }

        return errors;
    }

    /**
     * Validates the input for destroying a member.
     *
     * @param memberCode The code of the member to be destroyed.
     * @return A list of validation errors, if any.
     */
    public List<String> destroyMemberValidation(String memberCode) {
        List<String> errors = new ArrayList<>();

        if (memberCode == null) {
            errors.add("Member code is required");
        } else {
            boolean isValidMemberCode = memberService.isValidMemberCode(memberCode);
            if (!isValidMemberCode) {
                errors.add("Invalid member code");
            }
        }

        return errors;
    }

    /**
     * Validates the inputs for creating a transaction.
     *
     * @param transactionDTO The TransactionDTO object containing the transaction information.
     * @return A list of validation errors, if any.
     */
    public List<String>

    createTransactionValidation(TransactionDTO transactionDTO) {
        List<String> errors = new ArrayList<>();

        if (transactionDTO.getMember().getMemberCode() == null) {
            errors.add("Member code is required");
        } else {
            boolean isValidMemberCode = memberService.isValidMemberCode(transactionDTO.getMember().getMemberCode());
            if (!isValidMemberCode) {
                errors.add("Invalid member code");
            }
        }

        if (transactionDTO.getEstReturnedAt() == null) {
            errors.add("Estimated returned date is required");
        } else {
            if (!isValidDateFormat(transactionDTO.getEstReturnedAt())) {
                errors.add("Invalid estimated returned date format. It should be yyyy-MM-dd");
            }
        }

        if (transactionDTO.getOperatorName() == null) {
            errors.add("Operator name is required");
        }

        if (transactionDTO.getDetailTransactions() == null) {
            errors.add("Detail transactions is required");
        } else {
            if (transactionDTO.getDetailTransactions().size() == 0) {
                errors.add("Detail transactions cannot be empty");
            }
            if (transactionDTO.getDetailTransactions().size() > 0) {
                int totalBorrowedBook = 0;
                for (DetailTransactionDTO detailTransactionDTO : transactionDTO.getDetailTransactions()) {
                    if (detailTransactionDTO.getTotal() == null) {
                        errors.add("Total Borrowed Book is required");
                    } else {
                        if (detailTransactionDTO.getTotal() <= 0) {
                            errors.add("Total Borrowed Book cannot be negative");
                        } else {
                            BookDTO bookDTO = bookService.getBookByCode(detailTransactionDTO.getBook().getBookCode());
                            if (bookDTO.getStock() < detailTransactionDTO.getTotal()) {
                                errors.add("Total Borrowed Book for |" + bookDTO.getTitle() + "| is more than the stock (" + detailTransactionDTO.getTotal() + " > " + bookDTO.getStock() + ")");
                            } else if (detailTransactionDTO.getTotal() > applicationProperties.getAllowedBorrowSameBook()) {
                                errors.add("Total Borrowed Book for |" + bookDTO.getTitle() + "| cannot be more than " + applicationProperties.getAllowedBorrowSameBook() + " books. This is library policy.");
                            }
                        }
                    }
                    totalBorrowedBook += detailTransactionDTO.getTotal() == null ? 0 : detailTransactionDTO.getTotal();
                }
                if (totalBorrowedBook > applicationProperties.getAllowedTotalBorrowBook()) {
                    errors.add("Total Borrowed Books cannot be more than " + applicationProperties.getAllowedTotalBorrowBook() + " books. This is library policy.");
                }
            }
        }

        return errors;
    }

    /**
     * Validates the input for updating a transaction.
     *
     * @param transactionDTO The TransactionDTO object containing the transaction information.
     * @return A list of validation errors, if any.
     */
    public List<String> updateTransactionValidation(TransactionDTO transactionDTO) {
        List<String> errors = new ArrayList<>();

        if (transactionDTO.getTransactionCode() == null) {
            errors.add("Transaction code is required for update");
        } else {
            boolean isValidTransactionCode = transactionService.isValidTransactionCode(transactionDTO.getTransactionCode());
            if (!isValidTransactionCode) {
                errors.add("Invalid transaction code");
            }
        }

        return errors;
    }

    /**
     * Checks if the given date is in a valid format (yyyy-MM-dd).
     *
     * @param date The date to validate.
     * @return True if the date is in a valid format, false otherwise.
     */
    private boolean isValidDateFormat(String date) {
        // Regular expression for yyyy-MM-dd format
        String dateFormatRegex = "\\d{4}-\\d{2}-\\d{2}";
        return date.matches(dateFormatRegex);
    }
}