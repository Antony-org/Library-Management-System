package edu.com.librarymanagementsystem.borrowingRecords;

import edu.com.librarymanagementsystem.libraryUsers.LibraryUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BorrowingRepository extends JpaRepository<BorrowingRecord, Integer> {
    Optional<BorrowingRecord> findByBookIdAndPatronId(@Param("bookId")Integer bookId, @Param("patronId") Integer patronId);
}
