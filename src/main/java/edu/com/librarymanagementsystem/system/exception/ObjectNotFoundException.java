package edu.com.librarymanagementsystem.system.exception;

public class ObjectNotFoundException extends RuntimeException{

    public ObjectNotFoundException(String objectName, Integer id) {
        super("Could not find a " + objectName + " with Id " + id );
    }

    public ObjectNotFoundException(String objectName, String name) {
        super("Could not find a " + objectName + " with Id " + name );
    }

    public ObjectNotFoundException(Integer id, Integer patronId) {
        super("Borrowing record not found for book with id: " + id + " and patron with id: " + patronId);
    }
}