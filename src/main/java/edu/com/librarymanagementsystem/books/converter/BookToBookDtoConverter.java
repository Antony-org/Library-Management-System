package edu.com.librarymanagementsystem.books.converter;

import edu.com.librarymanagementsystem.books.Book;
import edu.com.librarymanagementsystem.books.dto.BookDTO;
import edu.com.librarymanagementsystem.patron.converter.PatronToPatronDTOConverter;
import edu.com.librarymanagementsystem.patron.dto.PatronDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BookToBookDtoConverter implements Converter<Book, BookDTO> {

    @Override
    public BookDTO convert(Book source) {

        BookDTO bookDTO = new BookDTO(source.getId(), source.getTitle(),
                source.getAuthor(), source.getPublicationYear(), source.getIsbn());

        return bookDTO;
    }
}
