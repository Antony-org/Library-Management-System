package edu.com.librarymanagementsystem.books;

import edu.com.librarymanagementsystem.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book findById(Integer bookId){
        return this.bookRepository.findById(bookId)
                .orElseThrow(() -> new ObjectNotFoundException("book", bookId));
    }

    public Book findByTitle(String title){
        return this.bookRepository.findByTitle(title)
                .orElseThrow(() -> new ObjectNotFoundException("book", title));
    }

    public List<Book> findAll(){
        return this.bookRepository.findAll();
    }

    public Book save(Book newBook) {
        return this.bookRepository.save(newBook);
    }

    public Book update(Integer bookId, Book update){
        Book book = this.bookRepository.findById(bookId).
                orElseThrow(() -> new ObjectNotFoundException("book", bookId));

        if(update.getTitle() != null){
            book.setTitle(update.getTitle());
        }
        if(update.getAuthor() != null){
            book.setAuthor(update.getAuthor());
        }
        if(update.getPublicationYear() != null){
            book.setPublicationYear(update.getPublicationYear());
        }
        if(update.getIsbn() != null){
            book.setIsbn(update.getIsbn());
        }
        return this.bookRepository.save(book);
    }

    public void deleteBook(Integer bookId){
         Book book = this.bookRepository.findById(bookId)
                 .orElseThrow(() -> new ObjectNotFoundException("book", bookId));
         this.bookRepository.deleteById(bookId);
    }

}
