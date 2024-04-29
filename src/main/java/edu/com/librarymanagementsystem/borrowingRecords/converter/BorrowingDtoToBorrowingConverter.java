package edu.com.librarymanagementsystem.borrowingRecords.converter;

import edu.com.librarymanagementsystem.books.Book;
import edu.com.librarymanagementsystem.books.BookService;
import edu.com.librarymanagementsystem.borrowingRecords.BorrowingRecord;
import edu.com.librarymanagementsystem.borrowingRecords.dto.BorrowingDto;
import edu.com.librarymanagementsystem.patron.Patron;
import edu.com.librarymanagementsystem.patron.PatronService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BorrowingDtoToBorrowingConverter implements Converter<BorrowingDto, BorrowingRecord> {
    private final BookService bookService;

    private final PatronService patronService;

    public BorrowingDtoToBorrowingConverter(BookService bookService, PatronService patronService) {
        this.bookService = bookService;
        this.patronService = patronService;
    }

    @Override
    public BorrowingRecord convert(BorrowingDto source) {
        Book book = bookService.findByTitle(source.bookTitle());
        Patron patron = patronService.findByName(source.patronName());
        return new BorrowingRecord(source.id(), book, patron, source.borrowingDate(), source.returnDate());
    }
}
