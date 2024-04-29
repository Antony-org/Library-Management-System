package edu.com.librarymanagementsystem.borrowingRecords.dto;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

public record BorrowingDto(Integer id,
                      @NotEmpty(message = "A Title is required")
                      String bookTitle,
                      @NotEmpty(message = "Patron's name is required")
                      String patronName,
                      @NotEmpty(message = "Borrowing date is required")
                      LocalDate borrowingDate,
                      LocalDate returnDate) {

    @Override
    public Integer id() {
        return id;
    }

}