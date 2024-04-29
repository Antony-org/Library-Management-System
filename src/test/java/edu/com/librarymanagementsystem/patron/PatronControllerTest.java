package edu.com.librarymanagementsystem.patron;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.com.librarymanagementsystem.books.Book;
import edu.com.librarymanagementsystem.borrowingRecords.BorrowingRecord;
import edu.com.librarymanagementsystem.system.StatusCode;
import edu.com.librarymanagementsystem.system.exception.ObjectNotFoundException;
import edu.com.librarymanagementsystem.patron.dto.PatronDTO;
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


import java.time.LocalDate;
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
class PatronControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PatronService patronService;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    List<Patron> patrons;

    @BeforeEach
    void setUp() {
        this.patrons = new ArrayList<>();

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
    void testFindPatronByIdSuccess() throws Exception {
        //Given
        given(this.patronService.findById(1)).willReturn(this.patrons.get(0));

        //When and Then
        this.mockMvc.perform(get(this.baseUrl + "/patrons/1").accept(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.flag").value(true)).
                andExpect(jsonPath("$.code").value(StatusCode.SUCCESS)).
                andExpect(jsonPath("$.message").value("Find One Success")).
                andExpect(jsonPath("$.data.id").value(1)).
                andExpect(jsonPath("$.data.name").value(patrons.get(0).getName()));
    }

    @Test
    void testFindPatronByIdNotFound() throws Exception {
        //Given
        given(this.patronService.findById(4)).willThrow(new ObjectNotFoundException("patron", 4));
        Integer id = 4;
        //When and Then
        this.mockMvc.perform(get(this.baseUrl + "/patrons/4").accept(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.flag").value(false)).
                andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND)).
                andExpect(jsonPath("$.message").value("Could not find a patron with Id " + id)).
                andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllPatronsSuccess() throws Exception {
        //Given
        given(this.patronService.findAll()).willReturn(this.patrons);

        //When and Then
        this.mockMvc.perform(get(this.baseUrl + "/patrons").accept(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.flag").value(true)).
                andExpect(jsonPath("$.code").value(StatusCode.SUCCESS)).
                andExpect(jsonPath("$.message").value("Find All Success")).
                andExpect(jsonPath("$.data", Matchers.hasSize(this.patrons.size()))).
                andExpect(jsonPath("$.data[0].name").value(patrons.get(0).getName()));
    }

    @Test
    void testAddPatronSuccess() throws Exception {
        //Given
        PatronDTO newPatron = new PatronDTO(7,"blabla","example@example.com", null, null);

        String json = objectMapper.writeValueAsString(newPatron);

        Patron savedPatron = new Patron();
        savedPatron.setId(4);
        savedPatron.setName("blabla");


        given(this.patronService.save(Mockito.any(Patron.class))).willReturn(savedPatron);

        //When and Then
        this.mockMvc.perform(post(this.baseUrl + "/patrons").contentType(MediaType.APPLICATION_JSON)
                .content(json).accept(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.flag").value(true)).
                andExpect(jsonPath("$.code").value(StatusCode.SUCCESS)).
                andExpect(jsonPath("$.message").value("Add Success")).
                andExpect(jsonPath("$.data.name").value(savedPatron.getName())).
                andExpect(jsonPath("$.data.id").isNotEmpty());
    }

    @Test
    void testUpdatePatronSuccess() throws Exception {
        //Given
        PatronDTO newPatron = new PatronDTO(7,"blabla","example@example.com", null, null);

        String json = objectMapper.writeValueAsString(newPatron);

        Patron updatedPatron = new Patron();
        updatedPatron.setId(4);
        updatedPatron.setName("blabla");

        given(this.patronService.update(eq(4), Mockito.any(Patron.class))).willReturn(updatedPatron);

        //When and Then
        this.mockMvc.perform(put(this.baseUrl + "/patrons/4").contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.flag").value(true)).
                andExpect(jsonPath("$.code").value(StatusCode.SUCCESS)).
                andExpect(jsonPath("$.message").value("Update Success")).
                andExpect(jsonPath("$.data.name").value(updatedPatron.getName())).
                andExpect(jsonPath("$.data.id").isNotEmpty());
    }

    @Test
    void testUpdatePatronNotFound() throws Exception {
        //Given
        PatronDTO newPatron = new PatronDTO(7,"blabla","example@example.com", null, null);

        String json = objectMapper.writeValueAsString(newPatron);

        given(this.patronService.update(eq(7), Mockito.any(Patron.class))).willThrow(new ObjectNotFoundException("patron", 7));

        //When and Then
        this.mockMvc.perform(put(this.baseUrl + "/patrons/7").contentType(MediaType.APPLICATION_JSON)
                        .content(json).accept(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.flag").value(false)).
                andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND)).
                andExpect(jsonPath("$.message").value("Could not find a patron with Id " + 7)).
                andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeletePatronByIdSuccess() throws Exception {
        //Given
        doNothing().when(this.patronService).delete(1);

        //When and Then
        this.mockMvc.perform(delete(this.baseUrl + "/patrons/1").accept(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.flag").value(true)).
                andExpect(jsonPath("$.code").value(StatusCode.SUCCESS)).
                andExpect(jsonPath("$.message").value("Delete Success")).
                andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeletePatronByIdNotFound() throws Exception {
        //Given
        doThrow(new ObjectNotFoundException("patron",1)).when(this.patronService).delete(1);

        //When and Then
        this.mockMvc.perform(delete(this.baseUrl + "/patrons/1").accept(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.flag").value(false)).
                andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND)).
                andExpect(jsonPath("$.message").value("Could not find a patron with Id " + 1)).
                andExpect(jsonPath("$.data").isEmpty());
    }

}
