package edu.com.librarymanagementsystem.libraryUsers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<LibraryUser, Integer> {

    Optional<LibraryUser> findByUsername(@Param("username") String username);

}