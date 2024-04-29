package edu.com.librarymanagementsystem.libraryUsers.converter;

import edu.com.librarymanagementsystem.libraryUsers.LibraryUser;
import edu.com.librarymanagementsystem.libraryUsers.Dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LibraryUserToUserDtoConverter implements Converter<LibraryUser, UserDto> {
    @Override
    public UserDto convert(LibraryUser source) {
        return new UserDto(source.getId(), source.getUsername(), source.isEnabled(), source.getRoles());
    }
}