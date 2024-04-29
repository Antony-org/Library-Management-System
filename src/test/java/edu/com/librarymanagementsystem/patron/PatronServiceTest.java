package edu.com.librarymanagementsystem.patron;

import edu.com.librarymanagementsystem.books.Book;
import edu.com.librarymanagementsystem.books.BookRepository;
import edu.com.librarymanagementsystem.borrowingRecords.BorrowingRecord;
import edu.com.librarymanagementsystem.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatronServiceTest {

    @Mock
    PatronRepository patronRepository;

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    PatronService patronService;

    List<Patron> patrons = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Book book1 = new Book(1, "Deluminator", "J.K. Rowling", "2001", "100");
        Book book2 = new Book(2, "Invisibility Cloak", "J.K. Rowling", "1998", "101");
        Book book3 = new Book(3, "Elder Wand", "J.K. Rowling", "2007", "102");
        Book book4 = new Book(4, "The Marauder's Map", "J.K. Rowling", "2005", "103");
        Book book5 = new Book(5, "The Sword Of Gryffindor", "J.K. Rowling", "2007", "104");
        Book book6 = new Book(6, "Resurrection Stone", "J.K. Rowling", "2007", "105");

        Patron patron1 = new Patron(1, "Hermione Granger", "hermione@example.com", "1234567890");
        Patron patron2 = new Patron(2, "Harry Potter", "harry@example.com", "9876543210");
        Patron patron3 = new Patron(3, "Neville Longbottom", "neville@example.com", "4567890123");

        BorrowingRecord borrowingRecord1 = new BorrowingRecord(1, book1, patron1, LocalDate.now().minusDays(7), null);
        BorrowingRecord borrowingRecord2 = new BorrowingRecord(2, book3, patron1, LocalDate.now().minusDays(14), null);
        BorrowingRecord borrowingRecord3 = new BorrowingRecord(3, book2, patron2, LocalDate.now().minusDays(10), null);
        BorrowingRecord borrowingRecord4 = new BorrowingRecord(4, book4, patron2, LocalDate.now().minusDays(5), LocalDate.now());
        BorrowingRecord borrowingRecord5 = new BorrowingRecord(5, book5, patron3, LocalDate.now().minusDays(12), null);

        this.patrons.add(patron1);
        this.patrons.add(patron2);
        this.patrons.add(patron3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        //Given. Arrange inputs and targets. Define the behaviour of Mock object PatronRepository
        Book book1 = new Book(1, "Deluminator", "J.K. Rowling", "2001", "100");
        Book book2 = new Book(2, "Invisibility Cloak", "J.K. Rowling", "1998", "101");

        Patron patron = new Patron();
        patron.setId(1);
        patron.setName("Harry Potter");

        BorrowingRecord borrowingRecord1 = new BorrowingRecord();
        borrowingRecord1.setId(1);
        patron.addBorrowingRecord(borrowingRecord1);


        given(patronRepository.findById(1)).willReturn(Optional.of(patron));

        //When
        Patron foundPatron = patronService.findById(1);

        //Then
        assertThat(foundPatron.getId()).isEqualTo(patron.getId());
        assertThat(foundPatron.getName()).isEqualTo(patron.getName());
        assertThat(foundPatron.getNumberOfBorrowedRecords()).isEqualTo(patron.getNumberOfBorrowedRecords());

        verify(patronRepository, times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        given(patronRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(()->{
            Patron foundPatron = patronService.findById(1);
        });

        // Then
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).
                hasMessage("Could not find patron with Id " + 1);

        verify(patronRepository, times(1)).findById(1);
    }

    @Test
    void testFindAllSuccess() {
        //Given. Arrange inputs and targets. Define the behaviour of Mock object PatronRepository
        given(patronRepository.findAll()).willReturn(this.patrons);

        //When
        List<Patron> foundPatrons = patronService.findAll();

        //Then
        assertThat(foundPatrons.get(0).getId()).isEqualTo(this.patrons.get(0).getId());
        assertThat(foundPatrons.get(0).getName()).isEqualTo(this.patrons.get(0).getName());
        assertThat(foundPatrons.size()).isEqualTo(this.patrons.size());

        verify(patronRepository, times(1)).findAll();
    }

    @Test
    void testSavePatronSuccess() {
        //Given. Arrange inputs and targets. Define the behaviour of Mock object PatronRepository
        Patron w = new Patron();
        w.setName("blabla");

        given(patronRepository.save(w)).willReturn(w);

        //When
        Patron savedPatron = this.patronService.save(w);

        //Then
        assertThat(savedPatron.getName()).isEqualTo(w.getName());

        verify(patronRepository, times(1)).save(w);
    }

    @Test
    void testUpdateSuccess() {
        //Given. Arrange inputs and targets. Define the behaviour of Mock object PatronRepository
        Patron w = new Patron();
        w.setId(1);
        w.setName("Harry Potter");

        given(patronRepository.findById(1)).willReturn(Optional.of(w));
        given(patronRepository.save(w)).willReturn(w);

        //When
        Patron foundPatron = patronService.update(1, w);

        //Then
        assertThat(foundPatron.getId()).isEqualTo(w.getId());
        assertThat(foundPatron.getName()).isEqualTo(w.getName());
        assertThat(foundPatron.getNumberOfBorrowedRecords()).isEqualTo(w.getNumberOfBorrowedRecords());

        verify(patronRepository, times(1)).findById(1);
        verify(patronRepository, times(1)).save(w);
    }

    @Test
    void testUpdateNotFound(){
        //Given
        Patron w = new Patron();
        w.setName("Harry Potter");

        given(this.patronRepository.findById(4)).willReturn(Optional.empty());

        //When
        assertThrows(ObjectNotFoundException.class, ()-> {
            this.patronService.update(4, w);
        });

        //Then
        verify(this.patronRepository, times(1)).findById(4);
    }

    @Test
    void testDeleteSuccess() {
        //Given. Arrange inputs and targets. Define the behaviour of Mock object PatronRepository
        Patron w = new Patron();
        w.setId(1);
        w.setName("Harry Potter");

        given(patronRepository.findById(1)).willReturn(Optional.of(w));
        doNothing().when(this.patronRepository).deleteById(1);

        //When
        patronService.delete(1);

        //Then
        verify(patronRepository, times(1)).findById(1);
        verify(patronRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound(){
        //Given

        given(this.patronRepository.findById(1)).willThrow(new ObjectNotFoundException("patron", 1));

        //When
        assertThrows(ObjectNotFoundException.class, ()-> {
            this.patronService.delete(1);
        });

        //Then
        verify(this.patronRepository, times(1)).findById(1);
    }

    /*@Test
    void testAssignBookSuccess() {
        //Given. Arrange inputs and targets. Define the behaviour of Mock object PatronRepository
        Book book = new Book();
        book.setId(1);
        book.setTitle("Deluminator");
        book.setAuthor("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        book.setIsbn("ImageUrl");

        Patron w = new Patron();
        w.setId(1);
        w.setName("Harry Potter");
        BorrowingRecord borrowingRecord1 = new BorrowingRecord(1, book, w, LocalDate.now().minusDays(7), null);

        w.addBorrowingRecord(borrowingRecord1);

        Patron w2 = new Patron();
        w2.setId(2);
        w2.setName("Neville Longbottom");

        given(this.patronRepository.findById(2)).willReturn(Optional.of(w2));
        given(this.bookRepository.findById(1)).willReturn(Optional.of(book));

        //When
        this.patronService.assignBook(2, 1);

        //Then
        assertThat(book.getOwner().getId()).isEqualTo(2);
        assertThat(w2.getBooks()).contains(book);
        assertThat(w.getNumberOfBooks()).isEqualTo(0);
        assertThat(w2.getNumberOfBooks()).isEqualTo(1);

        verify(this.patronRepository, times(1)).findById(2);
        verify(this.bookRepository, times(1)).findById("1250808601744904191");
    }*/

/*    @Test
    void testAssignBookErrorPatronNotFound(){
        //Given
        Patron w = new Patron();
        w.setName("Harry Potter");
        w.setId(1);

        Book book = new Book();
        book.setId("1250808601744904191");
        book.setName("Deluminator");
        book.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        book.setImageUrl("ImageUrl");

        w.addBook(book);

        given(this.patronRepository.findById(2)).willReturn(Optional.empty());
        //given(this.bookRepository.findById("1250808601744904191")).willReturn(Optional.of(book));

        //When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, ()-> {
            this.patronService.assignBook(2, "1250808601744904191");
        });

        //Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                        .hasMessage("Could not find patron with Id " + 2);

        assertThat(book.getOwner().getId()).isEqualTo(1);
        verify(this.patronRepository, times(1)).findById(2);
    }

    @Test
    void testAssignBookErrorBookNotFound(){
        //Given
        Patron w = new Patron();
        w.setName("Harry Potter");
        w.setId(1);

        Patron w2 = new Patron();
        w2.setId(2);
        w2.setName("Neville Longbottom");

        given(this.bookRepository.findById("1250808601744904191")).willReturn(Optional.empty());
        given(this.patronRepository.findById(2)).willReturn(Optional.of(w2));

        //When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, ()-> {
            this.patronService.assignBook(2, "1250808601744904191");
        });

        //Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find book with Id " + "1250808601744904191");

        assertThat(w.getNumberOfBooks()).isEqualTo(0);
        verify(this.patronRepository, times(1)).findById(2);
    }*/

}