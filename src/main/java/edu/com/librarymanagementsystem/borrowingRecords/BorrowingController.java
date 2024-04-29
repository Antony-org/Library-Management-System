package edu.com.librarymanagementsystem.borrowingRecords;

import edu.com.librarymanagementsystem.borrowingRecords.BorrowingRecord;
import edu.com.librarymanagementsystem.borrowingRecords.BorrowingService;
import edu.com.librarymanagementsystem.borrowingRecords.converter.BorrowingDtoToBorrowingConverter;
import edu.com.librarymanagementsystem.borrowingRecords.converter.BorrowingToBorrowingDtoConverter;
import edu.com.librarymanagementsystem.borrowingRecords.dto.BorrowingDto;
import edu.com.librarymanagementsystem.system.Result;
import edu.com.librarymanagementsystem.system.StatusCode;
import edu.com.librarymanagementsystem.system.exception.ObjectNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}")
@Tag(name = "Borrowing Records")
public class BorrowingController {
    private final BorrowingService borrowingService;

    private final BorrowingToBorrowingDtoConverter borrowingToBorrowingDtoConverter;

    private final BorrowingDtoToBorrowingConverter borrowingDtoToBorrowingConverter;

    public BorrowingController(BorrowingService borrowingService, BorrowingToBorrowingDtoConverter borrowingToBorrowingDtoConverter, BorrowingDtoToBorrowingConverter borrowingDtoToBorrowingConverter) {
        this.borrowingService = borrowingService;
        this.borrowingToBorrowingDtoConverter = borrowingToBorrowingDtoConverter;
        this.borrowingDtoToBorrowingConverter = borrowingDtoToBorrowingConverter;
    }

    @Operation(summary = "Borrow a book", description = "Borrow a book with the given bookId by a patron with the given patronId.")
    @ApiResponse(responseCode = "200", description = "Book borrowed successfully.")
    @ApiResponse(responseCode = "401", description = "Unauthorized access")
    @ApiResponse(responseCode = "404", description = "Book or patron not found with the specified IDs.")
    @PostMapping("/borrow/{bookId}/patrons/{patronId}")
    public Result borrowBook(@PathVariable("bookId") Integer bookId, @PathVariable("patronId") Integer patronId) {
        BorrowingRecord borrowingRecord = borrowingService.borrowBook(bookId, patronId);
        BorrowingDto borrowingDto = this.borrowingToBorrowingDtoConverter.convert(borrowingRecord);
        return new Result(true, StatusCode.SUCCESS, "Borrow Success", borrowingDto);
    }

    @Operation(summary = "Return a borrowed book", description = "Return a borrowed book with the given bookId by a patron with the given patronId.")
    @ApiResponse(responseCode = "200", description = "Book returned successfully.")
    @ApiResponse(responseCode = "401", description = "Unauthorized access")
    @ApiResponse(responseCode = "404", description = "Book or patron not found with the specified IDs.")
    @PutMapping("/return/{bookId}/patron/{patronId}")
    public Result returnBook(@PathVariable("bookId") Integer bookId, @PathVariable("patronId") Integer patronId) {
        BorrowingRecord borrowingRecord = borrowingService.returnBook(bookId, patronId);
        BorrowingDto borrowingDto = this.borrowingToBorrowingDtoConverter.convert(borrowingRecord);
        return new Result(true, StatusCode.SUCCESS, "Return Success", borrowingDto);
    }

    @Operation(summary = "Get all borrowing records", description = "Retrieves all borrowing records.")
    @ApiResponse(responseCode = "200", description = "Borrowing records retrieved successfully.")
    @ApiResponse(responseCode = "401", description = "Unauthorized access")
    @ApiResponse(responseCode = "404", description = "No borrowing records found.")
    @GetMapping
    public Result getAllBorrowingRecords() {
        List<BorrowingRecord> borrowingRecords = borrowingService.getAllBorrowingRecords();
        List<BorrowingDto> borrowingDtos = borrowingRecords.stream()
                .map(foundRecord -> this.borrowingToBorrowingDtoConverter.convert(foundRecord))
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", borrowingDtos);
    }

    @Operation(summary = "Get a borrowing record by ID", description = "Retrieves a borrowing record with the given ID.")
    @ApiResponse(responseCode = "200", description = "Borrowing record retrieved successfully.")
    @ApiResponse(responseCode = "401", description = "Unauthorized access")
    @ApiResponse(responseCode = "404", description = "Borrowing record not found with the specified ID.")
    @GetMapping("/borrow/{id}")
    public Result getBorrowingRecordById(@PathVariable("id") Integer id) {
        BorrowingRecord borrowingRecord = borrowingService.getBorrowingRecordById(id)
                .orElseThrow(() -> new ObjectNotFoundException("borrowing record", id));
        BorrowingDto borrowingDto = this.borrowingToBorrowingDtoConverter.convert(borrowingRecord);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", borrowingDto);
    }
}
