package edu.com.librarymanagementsystem.books;

import edu.com.librarymanagementsystem.patron.Patron;
import edu.com.librarymanagementsystem.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookService bookService;

    List<Book> books;

    @BeforeEach
    void setUp() {
        Book a1 = new Book();
        a1.setId(1);
        a1.setTitle("Deluminator");
        a1.setAuthor("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");

        Book a2 = new Book();
        a2.setId(2);
        a2.setTitle("Invisibility Cloak");
        a2.setAuthor("An invisibility cloak is used to make the wearer invisible.");

        this.books = new ArrayList<>();
        books.add(a1);
        books.add(a2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        //Given. Arrange inputs and targets. Define the behaviour of Mock object bookRepository
        /*
        "id": "1250808601744904192",
        "name": "Invisibility Cloak",
        "description": "An invisibility cloak is used to make the wearer invisible.",
        "imageUrl": "ImageUrl"
        */
        Book a = new Book();
        a.setId(2);
        a.setTitle("Invisibility Cloak");
        a.setAuthor("An invisibility cloak is used to make the wearer invisible.");

        Patron patron = new Patron();
        patron.setId(2);
        patron.setName("Harry Potter");


        // Defines the behaviour of Mock object
        given(bookRepository.findById(2)).willReturn(Optional.of(a));

        //When. Act on the target behaviour. Cover the methods to be tested
        Book foundBook = bookService.findById(2);

        //Then. Assert expected outcomes
        assertThat(foundBook.getId()).isEqualTo(a.getId());
        assertThat(foundBook.getTitle()).isEqualTo(a.getTitle());
        assertThat(foundBook.getAuthor()).isEqualTo(a.getAuthor());

        verify(bookRepository, times(1)).findById(2);

    }

    @Test
    void testFindByIdNotFound() {
        // Given
        given(bookRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(()->{
            Book foundBook = bookService.findById(2);
        });

        // Then
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).
                hasMessage("Could not find book with Id 2");

        verify(bookRepository, times(1)).findById(2);
    }

    @Test
    void testFindAllSuccess(){
        // Given
        given(bookRepository.findAll()).willReturn(this.books);

        // When
        List<Book> foundBooks = bookService.findAll();

        // Then
        assertThat(foundBooks.size()).isEqualTo(this.books.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testCreatebookSuccess(){
        // Given
        Book newBook = new Book();
        newBook.setTitle("Book 7");
        newBook.setAuthor("desc");

        given(this.bookRepository.save(newBook)).willReturn(newBook);

        // When
        Book createdBook = bookService.save(newBook);
        // Then

        //assertThat(createdBook.getId()).isEqualTo(7);
        assertThat(createdBook.getTitle()).isEqualTo(newBook.getTitle());
        assertThat(createdBook.getAuthor()).isEqualTo(newBook.getAuthor());
        verify(bookRepository, times(1)).save(newBook);
    }

    @Test
    void testUpdateSuccess(){
        //Given
        Book oldBook = new Book();
        oldBook.setId(2);
        oldBook.setTitle("Invisibility Cloak");
        oldBook.setAuthor("desc");

        Book update = new Book();
        //update.setId("1250808601744904192");
        update.setTitle("Invisibility Cloak");
        update.setAuthor("new desc");

        given(bookRepository.findById(2)).willReturn(Optional.of(oldBook));
        given(bookRepository.save(oldBook)).willReturn(oldBook);

        //When
        Book updatedBook = bookService.update(2, update);

        //Then
        assertThat(updatedBook.getId()).isEqualTo(2);
        assertThat(updatedBook.getAuthor()).isEqualTo(update.getAuthor());
        verify(bookRepository, times(1)).save(oldBook);
        verify(bookRepository, times(1)).findById(2);
    }

    @Test
    void testUpdateNotFound(){
        //Given
        Book update = new Book();
        update.setTitle("Invisibility Cloak");
        update.setAuthor("new desc");

        given(bookRepository.findById(2)).willReturn(Optional.empty());

        //When
        assertThrows(ObjectNotFoundException.class, ()-> {
            bookService.update(2, update);
        });

        //Then
        verify(bookRepository, times(1)).findById(2);
    }

    @Test
    void testDeleteSuccess(){
        //Given
        Book a = new Book();
        a.setId(2);
        a.setTitle("Invisibility Cloak");
        a.setAuthor("An invisibility cloak is used to make the wearer invisible.");
        // Defines the behaviour of Mock object
        given(bookRepository.findById(2)).willReturn(Optional.of(a));
        doNothing().when(bookRepository).deleteById(2);

        //When. Act on the target behaviour. Cover the methods to be tested
        bookService.deleteBook(2);

        //Then. Assert expected outcomes
        verify(bookRepository, times(1)).deleteById(2);
    }

    @Test
    void testDeleteNotFound(){
        //Given

        // Defines the behaviour of Mock object
        given(bookRepository.findById(2)).willReturn(Optional.empty());

        //When. Act on the target behaviour. Cover the methods to be tested
        assertThrows(ObjectNotFoundException.class, ()-> {
            bookService.deleteBook(2);
        });
        //Then. Assert expected outcomes
        verify(bookRepository, times(1)).findById(2);
    }

}