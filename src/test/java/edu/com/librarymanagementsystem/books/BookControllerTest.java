package edu.com.librarymanagementsystem.books;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.com.librarymanagementsystem.books.dto.BookDTO;
import edu.com.librarymanagementsystem.system.StatusCode;
import edu.com.librarymanagementsystem.system.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookService bookService;

    @Autowired
    ObjectMapper objectMapper;

    List<Book> bookList;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        this.bookList = new ArrayList<>();

        Book a1 = new Book(1, "Deluminator", "J.K. Rowling", "2001", "100");
        this.bookList.add(a1);

        Book a2 = new Book(2, "Invisibility Cloak", "J.K. Rowling", "1997", "101");
        this.bookList.add(a2);

        Book a3 = new Book(3, "Elder Wand", "J.K. Rowling", "1997", "102");
        this.bookList.add(a3);

        Book a4 = new Book(4, "The Marauder's Map", "J.K. Rowling", "1997", "103");
        this.bookList.add(a4);

        Book a5 = new Book(5, "The Sword Of Gryffindor", "J.K. Rowling", "1997", "104");
        this.bookList.add(a5);

        Book a6 = new Book(6, "Resurrection Stone", "J.K. Rowling", "1997", "105");
        this.bookList.add(a6);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindBookByIdSuccess() throws Exception {

        //Given
        given(this.bookService.findById(1)).willReturn(this.bookList.get(0));

        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/books/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Deluminator"));

    }

    @Test
    void testFindBookByIdNotFound() throws Exception {
        //Given
        given(this.bookService.findById(1)).willThrow(new ObjectNotFoundException("book", 1));

        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/books/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find book with Id 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testfindAllBooksSuccess() throws Exception {
        //Given
        given(this.bookService.findAll()).willReturn(this.bookList);
        //When and then
        this.mockMvc.perform(get(this.baseUrl + "/books").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.bookList.size())))
                .andExpect(jsonPath("$.data[0].id").value(1));
    }

    @Test
    void testCreateBookSuccess() throws Exception {
        //Given
        BookDTO newBook = new BookDTO(5, "Remembrall",
                "A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.",
                "100", null);

        String json = objectMapper.writeValueAsString(newBook);

        Book savedBook = new Book();
        savedBook.setId(5);
        savedBook.setTitle("Remembrall");
        savedBook.setAuthor("A Remembrall was a magical large marble-sized glass ball that contained smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered.");
        savedBook.setPublicationYear("100");
        savedBook.setIsbn(null);

        given(this.bookService.save(Mockito.any(Book.class))).willReturn(savedBook);

        //When and Then
        this.mockMvc.perform(post(this.baseUrl + "/books").contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.title").value(savedBook.getTitle()))
                .andExpect(jsonPath("$.data.author").value(savedBook.getAuthor()));
    }

    @Test
    void testUpdateBookSuccess() throws Exception {
        //Given
        BookDTO newBook = new BookDTO(1, "Remembrall",
                "new desc",
                "2005", null);

        String json = objectMapper.writeValueAsString(newBook);

        Book updatedBook = new Book();
        updatedBook.setId(1);
        updatedBook.setTitle("Invisibility Cloak");
        updatedBook.setAuthor("A new desc");
        updatedBook.setPublicationYear("200");

        given(this.bookService.update(eq(1), Mockito.any(Book.class))).willReturn(updatedBook);

        //When and Then
        this.mockMvc.perform(put(this.baseUrl + "/books/1").contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value(updatedBook.getTitle()));

    }

    @Test
    void testUpdateBookNotFound() throws Exception {
        //Given
        BookDTO newBook = new BookDTO(7, "Remembrall",
                "new desc",
                "2000", null);

        String json = objectMapper.writeValueAsString(newBook);



        given(this.bookService.update(eq(7), Mockito.any(Book.class))).willThrow(new ObjectNotFoundException("book", 7));

        //When and Then
        this.mockMvc.perform(put(this.baseUrl + "/books/7").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find book with Id 7"))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    void testDeleteBookSuccess() throws Exception {
        //Given
        doNothing().when(this.bookService).deleteBook(1);

        //When and Then
        this.mockMvc.perform(delete(this.baseUrl + "/books/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteBookNotFound() throws Exception {
        //Given
        doThrow(new ObjectNotFoundException("book", 1)).when(this.bookService).deleteBook(1);

        //When and Then
        this.mockMvc.perform(delete(this.baseUrl + "/books/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find book with Id 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}