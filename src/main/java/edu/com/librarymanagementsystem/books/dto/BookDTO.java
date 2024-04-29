package edu.com.librarymanagementsystem.books.dto;

import edu.com.librarymanagementsystem.patron.dto.PatronDTO;
import jakarta.validation.constraints.NotEmpty;

public record BookDTO(
                      Integer id,
                      String title,
                      String author,
                      String publicationYear,
                      String isbn) {
}
