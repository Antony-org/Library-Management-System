package edu.com.librarymanagementsystem.borrowingRecords;

import edu.com.librarymanagementsystem.books.Book;
import edu.com.librarymanagementsystem.books.BookRepository;
import edu.com.librarymanagementsystem.books.BookService;
import edu.com.librarymanagementsystem.patron.Patron;
import edu.com.librarymanagementsystem.patron.PatronRepository;
import edu.com.librarymanagementsystem.patron.PatronService;
import edu.com.librarymanagementsystem.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class BorrowingServiceTest {

    @Mock
    private BorrowingRepository borrowingRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private BookService bookService;

    @Mock
    private PatronService patronService;

    @InjectMocks
    private BorrowingService borrowingService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBorrowBook() {
        // Arrange
        Integer bookId = 1;
        Integer patronId = 2;
        Book book = new Book(bookId, "Book 1", "Author 1", "2023", "123456");
        Patron patron = new Patron(patronId, "John Doe", "john@example.com", "1234567890");
        BorrowingRecord expectedBorrowingRecord = new BorrowingRecord(1, book, patron, LocalDate.now(), null);

        // Given
        given(bookRepository.findById(1)).willReturn(Optional.of(book));
        given(patronRepository.findById(2)).willReturn(Optional.of(patron));

        //When. Act on the target behaviour. Cover the methods to be tested
        BorrowingRecord borrowingRecord2 = borrowingService.borrowBook(1, 2);

        //Then. Assert expected outcomes
        assertThat(borrowingRecord2.getBook().getId()).isEqualTo(book.getId());
        assertThat(borrowingRecord2.getBook().getTitle()).isEqualTo(book.getTitle());
        assertThat(borrowingRecord2.getPatron().getName()).isEqualTo(patron.getName());

        verify(bookRepository, times(1)).findById(1);
        verify(patronRepository, times(1)).findById(2);
    }

    @Test
    void testReturnBook() {
        // Arrange
        Integer bookId = 1;
        Integer patronId = 2;
        Book book = new Book(bookId, "Book 1", "Author 1", "2023", "123456");
        Patron patron = new Patron(patronId, "John Doe", "john@example.com", "1234567890");
        BorrowingRecord borrowingRecord = new BorrowingRecord(1, book, patron, LocalDate.now().minusDays(7), null);

        Mockito.when(borrowingRepository.findByBookIdAndPatronId(bookId, patronId)).thenReturn(Optional.of(borrowingRecord));
        Mockito.when(borrowingRepository.save(Mockito.any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        // Act
        BorrowingRecord returnedBorrowingRecord = borrowingService.returnBook(bookId, patronId);

        // Assert
        Assertions.assertNotNull(returnedBorrowingRecord.getReturnDate());
        Mockito.verify(borrowingRepository, Mockito.times(1)).findByBookIdAndPatronId(bookId, patronId);
        Mockito.verify(borrowingRepository, Mockito.times(1)).save(Mockito.any(BorrowingRecord.class));
    }

    @Test
    void testGetAllBorrowingRecords() {
        // Arrange
        List<BorrowingRecord> expectedBorrowingRecords = Arrays.asList(
                new BorrowingRecord(1, new Book(1, "Book 1", "Author 1", "2023", "123456"), new Patron(1, "John Doe", "john@example.com", "1234567890"), LocalDate.now().minusDays(7), null),
                new BorrowingRecord(2, new Book(2, "Book 2", "Author 2", "2024", "234567"), new Patron(2, "Jane Doe", "jane@example.com", "0987654321"), LocalDate.now().minusDays(14), LocalDate.now())
        );

        Mockito.when(borrowingRepository.findAll()).thenReturn(expectedBorrowingRecords);

        // Act
        List<BorrowingRecord> actualBorrowingRecords = borrowingService.getAllBorrowingRecords();

        // Assert
        Assertions.assertEquals(expectedBorrowingRecords, actualBorrowingRecords);
        Mockito.verify(borrowingRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testGetBorrowingRecordById() {
        // Arrange
        Integer borrowingRecordId = 1;
        Book book = new Book(1, "Book 1", "Author 1", "2023", "123456");
        Patron patron = new Patron(1, "John Doe", "john@example.com", "1234567890");
        BorrowingRecord expectedBorrowingRecord = new BorrowingRecord(borrowingRecordId, book, patron, LocalDate.now().minusDays(7), null);

        Mockito.when(borrowingRepository.findById(borrowingRecordId)).thenReturn(Optional.of(expectedBorrowingRecord));

        // Act
        Optional<BorrowingRecord> actualBorrowingRecord = borrowingService.getBorrowingRecordById(borrowingRecordId);

        // Assert
        Assertions.assertTrue(actualBorrowingRecord.isPresent());
        Assertions.assertEquals(expectedBorrowingRecord, actualBorrowingRecord.get());
        Mockito.verify(borrowingRepository, Mockito.times(1)).findById(borrowingRecordId);
    }

    @Test
    void testBorrowBookWithInvalidBook() {
        // Arrange
        Integer bookId = 1;
        Integer patronId = 2;

        //given(bookService.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        //When
        assertThrows(ObjectNotFoundException.class, ()-> {
            borrowingService.borrowBook(bookId, patronId);
        });

        //Then
        verify(borrowingRepository, times(1)).findById(2);
    }

    @Test
    void testBorrowBookWithInvalidPatron() {
        // Arrange
        Integer bookId = 1;
        Integer patronId = 2;
        Book book = new Book(bookId, "Book 1", "Author 1", "2023", "123456");

        Mockito.when(bookService.findById(bookId)).thenReturn(book);
        Mockito.when(patronService.findById(patronId)).thenReturn(null);

        // Act and Assert
        Assertions.assertThrows(ObjectNotFoundException.class, () -> borrowingService.borrowBook(bookId, patronId));
        Mockito.verify(bookService, Mockito.times(1)).findById(bookId);
        Mockito.verify(patronService, Mockito.times(1)).findById(patronId);
        Mockito.verify(borrowingRepository, Mockito.times(0)).save(Mockito.any(BorrowingRecord.class));
    }

    @Test
    void testReturnBookWithInvalidBorrowingRecord() {
        // Arrange
        Integer bookId = 1;
        Integer patronId = 2;

        Mockito.when(borrowingRepository.findByBookIdAndPatronId(bookId, patronId)).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(ObjectNotFoundException.class, () -> borrowingService.returnBook(bookId, patronId));
        Mockito.verify(borrowingRepository, Mockito.times(1)).findByBookIdAndPatronId(bookId, patronId);
        Mockito.verify(borrowingRepository, Mockito.times(0)).save(Mockito.any(BorrowingRecord.class));
    }
}