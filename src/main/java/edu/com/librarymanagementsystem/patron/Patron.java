package edu.com.librarymanagementsystem.patron;

import edu.com.librarymanagementsystem.books.Book;
import edu.com.librarymanagementsystem.borrowingRecords.BorrowingRecord;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patrons")
public class Patron implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Name is required.")
    private String name;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;

    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number")
    private String phoneNumber;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "patron")
    private List<BorrowingRecord> borrowingRecords = new ArrayList<>();

    public Patron() {
    }

    public Patron(Integer id, String name, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BorrowingRecord> getBorrowingRecords() {
        return this.borrowingRecords;
    }

    public void setBorrowingRecords(List<BorrowingRecord> borrowingRecords) {
        this.borrowingRecords = borrowingRecords;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void addBorrowingRecord(BorrowingRecord borrowingRecord) {
        borrowingRecord.setPatron(this);
        this.borrowingRecords.add((borrowingRecord));
    }

    public void removeAllBorrowingRecords(){
        this.borrowingRecords.stream().forEach(borrowingRecord -> borrowingRecord.setPatron(null));
        this.borrowingRecords = null;
    }

    public int getNumberOfBorrowedRecords() {
        return borrowingRecords.size();
    }
}
