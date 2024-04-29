package edu.com.librarymanagementsystem.system;

import edu.com.librarymanagementsystem.books.Book;
import edu.com.librarymanagementsystem.books.BookRepository;
import edu.com.librarymanagementsystem.borrowingRecords.BorrowingRecord;
import edu.com.librarymanagementsystem.borrowingRecords.BorrowingRepository;
import edu.com.librarymanagementsystem.libraryUsers.LibraryUser;
import edu.com.librarymanagementsystem.libraryUsers.UserService;
import edu.com.librarymanagementsystem.patron.Patron;
import edu.com.librarymanagementsystem.patron.PatronRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final BorrowingRepository borrowingRepository;

    private final BookRepository bookRepository;

    private final PatronRepository patronRepository;

    private final UserService userService;

    public DBDataInitializer(BorrowingRepository borrowingRepository, BookRepository bookRepository, PatronRepository patronRepository, UserService userService) {
        this.borrowingRepository = borrowingRepository;
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        Book book1 = new Book(1, "Deluminator", "J.K. Rowling", "2001", "100");
        Book book2 = new Book(2, "Invisibility Cloak", "J.K. Rowling", "1998", "101");
        Book book3 = new Book(3, "Elder Wand", "J.K. Rowling", "2007", "102");
        Book book4 = new Book(4, "The Marauder's Map", "J.K. Rowling", "2005", "103");
        Book book5 = new Book(5, "The Sword Of Gryffindor", "J.K. Rowling", "2007", "104");
        Book book6 = new Book(6, "Resurrection Stone", "J.K. Rowling", "2007", "105");

        bookRepository.saveAll(Arrays.asList(book1, book2, book3, book4, book5, book6));

        Patron patron1 = new Patron(1, "Hermione Granger", "hermione@example.com", "1234567890");
        Patron patron2 = new Patron(2, "Harry Potter", "harry@example.com", "9876543210");
        Patron patron3 = new Patron(3, "Neville Longbottom", "neville@example.com", "4567890123");

        patronRepository.saveAll(Arrays.asList(patron1, patron2, patron3));

        // Create borrowing records
        BorrowingRecord borrowingRecord1 = new BorrowingRecord(1, book1, patron1, LocalDate.now().minusDays(7), null);
        BorrowingRecord borrowingRecord2 = new BorrowingRecord(2, book3, patron1, LocalDate.now().minusDays(14), null);
        BorrowingRecord borrowingRecord3 = new BorrowingRecord(3, book2, patron2, LocalDate.now().minusDays(10), null);
        BorrowingRecord borrowingRecord4 = new BorrowingRecord(4, book4, patron2, LocalDate.now().minusDays(5), LocalDate.now());
        BorrowingRecord borrowingRecord5 = new BorrowingRecord(5, book5, patron3, LocalDate.now().minusDays(12), null);

        borrowingRepository.saveAll(Arrays.asList(borrowingRecord1, borrowingRecord2, borrowingRecord3, borrowingRecord4, borrowingRecord5));

        // Create users
        LibraryUser user1 = new LibraryUser(1, "john", "123456", true, "admin user");
        LibraryUser user2 = new LibraryUser(2, "eric", "654321", true, "admin user");
        LibraryUser user3 = new LibraryUser(3, "tom", "qwerty", false, "admin user");

        userService.save(user1);
        userService.save(user2);
        userService.save(user3);
    }
}
