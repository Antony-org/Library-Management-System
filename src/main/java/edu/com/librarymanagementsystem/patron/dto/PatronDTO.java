package edu.com.librarymanagementsystem.patron.dto;

import jakarta.validation.constraints.NotEmpty;

public record PatronDTO(Integer id,
                        String name,
                        String email,
                        String phoneNumber,
                        Integer NumOfBooks) {
}
