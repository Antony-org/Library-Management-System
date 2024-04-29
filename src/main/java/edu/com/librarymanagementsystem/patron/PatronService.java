package edu.com.librarymanagementsystem.patron;

import edu.com.librarymanagementsystem.books.Book;
import edu.com.librarymanagementsystem.books.BookRepository;
import edu.com.librarymanagementsystem.libraryUsers.MyUserPrincipal;
import edu.com.librarymanagementsystem.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class PatronService{

    private final PatronRepository patronRepository;

    private final BookRepository bookRepository;

    private final PasswordEncoder passwordEncoder;

    public PatronService(PatronRepository patronRepository, BookRepository bookRepository, PasswordEncoder passwordEncoder) {
        this.patronRepository = patronRepository;
        this.bookRepository = bookRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Patron findById(Integer patronId){
        return this.patronRepository.findById(patronId)
                .orElseThrow(() -> new ObjectNotFoundException("patron", patronId));
    }

    public Patron findByName(String name){
        return this.patronRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("patron", name));
    }

    public List<Patron> findAll(){
        return this.patronRepository.findAll();
    }

    public Patron save(Patron patron){
        return this.patronRepository.save(patron);
    }

    public Patron update(Integer patronId, Patron patron){
        Patron foundPatron = this.patronRepository.findById(patronId)
                .orElseThrow(() -> new ObjectNotFoundException("patron", patronId));
        foundPatron.setName(patron.getName());
        if (patron.getEmail() != null) {
            foundPatron.setEmail(patron.getEmail());
        }
        if (patron.getPhoneNumber() != null) {
            foundPatron.setPhoneNumber(patron.getPhoneNumber());
        }

        return this.patronRepository.save(foundPatron);
    }

    public void delete(Integer patronId){
        Patron foundPatron = this.patronRepository.findById(patronId)
                .orElseThrow(() -> new ObjectNotFoundException("patron", patronId));

        //foundPatron.removeAllBorrowingRecords();
        this.patronRepository.deleteById(patronId);
    }

/*    public void assignBook(Integer patronId, String bookId){
        // Find patron by ID from DB
        Patron patron = this.patronRepository.findById(patronId)
                .orElseThrow(() -> new ObjectNotFoundException("patron", patronId));
        // Find book by ID from DB
        Book book = this.bookRepository.findById(bookId)
                .orElseThrow(() -> new ObjectNotFoundException("book", bookId));
        // Transfer book ownership
        if (book.getOwner() != null) {
            book.getOwner().removeBook(book);
        }
        patron.addBook(book);
    }*/


}
