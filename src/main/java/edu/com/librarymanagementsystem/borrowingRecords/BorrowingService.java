package edu.com.librarymanagementsystem.borrowingRecords;

import edu.com.librarymanagementsystem.books.Book;
import edu.com.librarymanagementsystem.books.BookRepository;
import edu.com.librarymanagementsystem.books.BookService;
import edu.com.librarymanagementsystem.patron.Patron;
import edu.com.librarymanagementsystem.patron.PatronRepository;
import edu.com.librarymanagementsystem.patron.PatronService;
import edu.com.librarymanagementsystem.system.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowingService {
    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;

    @Autowired
    public BorrowingService(BorrowingRepository borrowingRecordRepository, BookRepository bookRepository, PatronRepository patronRepository) {
        this.borrowingRepository = borrowingRecordRepository;
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;

    }

    public BorrowingRecord save(Integer bookId, Integer patronId){
        BorrowingRecord borrowingRecord = new BorrowingRecord();

        borrowingRecord.setBorrowingDate(LocalDate.now());
        return this.borrowingRepository.save(borrowingRecord);
    }

    @Transactional
    public BorrowingRecord borrowBook(Integer bookId, Integer patronId) {
        // Find book by ID from DB
        Book book = this.bookRepository.findById(bookId).
                orElseThrow(() -> new ObjectNotFoundException("book", bookId));
        // Find patron by ID from DB
        Patron patron = this.patronRepository.findById(patronId).
                orElseThrow(() -> new ObjectNotFoundException("patron", patronId));

        BorrowingRecord borrowingRecord = new BorrowingRecord();

        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowingDate(LocalDate.now());

        patron.addBorrowingRecord(borrowingRecord);
        book.addBorrowingRecord(borrowingRecord);

        BorrowingRecord saved = this.borrowingRepository.save(borrowingRecord);
        System.out.println(saved.getBook().getId());

        return saved;
    }

    @Transactional
    public BorrowingRecord returnBook(Integer bookId, Integer patronId) {
        BorrowingRecord borrowingRecord = borrowingRepository.findByBookIdAndPatronId(bookId, patronId)
                .orElseThrow(() -> new ObjectNotFoundException(bookId ,patronId));
        if (borrowingRecord.getReturnDate() != null) {
            borrowingRecord.setReturnDate(LocalDate.now());
        }
        return borrowingRepository.save(borrowingRecord);
    }

    public List<BorrowingRecord> getAllBorrowingRecords() {
        return borrowingRepository.findAll();
    }

    public Optional<BorrowingRecord> getBorrowingRecordById(Integer id) {
        return borrowingRepository.findById(id);
    }
}