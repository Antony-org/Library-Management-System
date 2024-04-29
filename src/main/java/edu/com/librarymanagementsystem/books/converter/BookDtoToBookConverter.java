package edu.com.librarymanagementsystem.books.converter;

import edu.com.librarymanagementsystem.books.Book;
import edu.com.librarymanagementsystem.books.dto.BookDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BookDtoToBookConverter implements Converter<BookDTO, Book> {

    @Override
    public Book convert(BookDTO source) {

        Book book = new Book();
        book.setId(source.id());
        book.setTitle(source.title());
        book.setAuthor(source.author());
        book.setPublicationYear(source.publicationYear());
        book.setIsbn(source.isbn());
        return book;
    }
}
