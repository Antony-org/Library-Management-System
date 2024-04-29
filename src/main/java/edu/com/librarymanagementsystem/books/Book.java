package edu.com.librarymanagementsystem.books;

import edu.com.librarymanagementsystem.borrowingRecords.BorrowingRecord;
import edu.com.librarymanagementsystem.patron.Patron;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty(message = "Title is required.")
    @Column(nullable = false)
    private String title;

    @NotEmpty(message = "Author is required.")
    @Column(nullable = false)
    private String author;

    private String publicationYear;

    private String isbn;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BorrowingRecord> borrowingRecords = new HashSet<>();

    public Book() {
    }

    public Book(Integer id, String title, String author, String publicationYear, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Set<BorrowingRecord> getBorrowingRecords() {
        return borrowingRecords;
    }

    public void setBorrowingRecords(Set<BorrowingRecord> borrowingRecords) {
        this.borrowingRecords = borrowingRecords;
    }

    public void addBorrowingRecord(BorrowingRecord borrowingRecord) {
        borrowingRecord.setBook(this);
        this.borrowingRecords.add((borrowingRecord));
    }

    public void removeAllBorrowingRecords(){
        this.borrowingRecords.stream().forEach(borrowingRecord -> borrowingRecord.setBook(null));
        this.borrowingRecords = null;
    }

    public void removeBorrowingRecord(BorrowingRecord borrowingRecord) {
        // Remove book ownership
        borrowingRecord.setBook(null);
        this.borrowingRecords.remove(borrowingRecord);
    }

    public int getNumberOfBorrowedRecords() {
        return borrowingRecords.size();
    }
}
