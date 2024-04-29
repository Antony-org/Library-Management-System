package edu.com.librarymanagementsystem.books;

import edu.com.librarymanagementsystem.books.converter.BookDtoToBookConverter;
import edu.com.librarymanagementsystem.books.converter.BookToBookDtoConverter;
import edu.com.librarymanagementsystem.books.dto.BookDTO;
import edu.com.librarymanagementsystem.system.Result;
import edu.com.librarymanagementsystem.system.StatusCode;
import edu.com.librarymanagementsystem.system.exception.ObjectNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/books")
@Tag(name = "Books")
public class BookController{

    private final BookService bookService;

    private final BookToBookDtoConverter bookToBookDtoConverter;

    private final BookDtoToBookConverter bookDtoToBookConverter;


    public BookController(BookService bookService, BookToBookDtoConverter bookToBookDtoConverter, BookDtoToBookConverter bookDtoToBookConverter) {
        this.bookService = bookService;
        this.bookToBookDtoConverter = bookToBookDtoConverter;
        this.bookDtoToBookConverter = bookDtoToBookConverter;
    }

    @Operation(summary = "Get book by Id", description = "Retrieves a book that has the given Id.")
    @ApiResponse(responseCode = "200", description = "Book retrieved successfully.")
    @ApiResponse(responseCode = "401", description = "Unauthorized access")
    @ApiResponse(responseCode = "404", description = "No books found with the specified parameters")
    @GetMapping("/{bookId}")
    public Result findBookById(@PathVariable Integer bookId){
        Book foundBook = this.bookService.findById(bookId);
        BookDTO bookDTO = this.bookToBookDtoConverter.convert(foundBook);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", bookDTO);
    }

    @Operation(summary = "Get all books from the library", description = "Retrieves all books.")
    @ApiResponse(description = "The Found Books"
            , content = { @Content(schema = @Schema(implementation = BookDTO.class), mediaType = "application/json") })
    @ApiResponse(responseCode = "200", description = "Books retrieved successfully.")
    @ApiResponse(responseCode = "401", description = "Unauthorized access")
    @ApiResponse(responseCode = "404", description = "No books found")
    @GetMapping
    public Result findALl(){
        List<Book> foundBooks = this.bookService.findAll();

        // convert found Books to a list of bookDTOS
        List<BookDTO> bookDTOS = foundBooks.stream().
                map(foundBook -> this.bookToBookDtoConverter.convert(foundBook)).
                collect(Collectors.toList());

        return new Result(true, StatusCode.SUCCESS, "Find All Success", bookDTOS);
    }

    @Operation(summary = "Add a book to the library", description = "Creates a book with the given parameters.")
    @ApiResponse(description = "The Added Book"
            , content = { @Content(schema = @Schema(implementation = BookDTO.class), mediaType = "application/json") })
    @ApiResponse(responseCode = "200", description = "Book added successfully")
    @ApiResponse(responseCode = "400", description = "Bad request: Essential parameters are missing")
    @ApiResponse(responseCode = "401", description = "Unauthorized access")
    @ApiResponse(responseCode = "404", description = "No books found with the specified parameters")
    @PostMapping
    public Result addBook(@Valid @RequestBody BookDTO bookDTO){
        // Convert BookDTO to Book
        Book newBook = this.bookDtoToBookConverter.convert(bookDTO);
        Book savedBook = this.bookService.save(newBook);

        // Convert again to BookDTO
        BookDTO savedBookDTO = this.bookToBookDtoConverter.convert(savedBook);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedBookDTO);
    }

    @Operation(summary = "Update a book in the library", description = "Updates a book with the given parameters.")
    @ApiResponse(responseCode = "200", description = "Book updated successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized access")
    @ApiResponse(responseCode = "404", description = "No books found with the specified parameters")
    @PutMapping("/{bookId}")
    public Result updateBook(@PathVariable Integer bookId, @Valid @RequestBody BookDTO bookDTO){
        Book update = this.bookDtoToBookConverter.convert(bookDTO);

        Book updatedBook = this.bookService.update(bookId, update);
        BookDTO updatedBookDto = this.bookToBookDtoConverter.convert(updatedBook);

        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedBookDto);
    }

    @Operation(summary = "Delete a book from the library", description = "Deletes a book with the given Id.")
    @ApiResponse(responseCode = "200", description = "Book deleted successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized access")
    @ApiResponse(responseCode = "404", description = "No books found with the specified parameters")
    @DeleteMapping("/{bookId}")
    public Result deleteBook(@PathVariable Integer bookId){
        this.bookService.deleteBook(bookId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
