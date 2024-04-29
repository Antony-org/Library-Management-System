package edu.com.librarymanagementsystem.libraryUsers.converter;

import edu.com.librarymanagementsystem.libraryUsers.LibraryUser;
import edu.com.librarymanagementsystem.libraryUsers.Dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToLibraryUserConverter implements Converter<UserDto, LibraryUser> {
    @Override
    public LibraryUser convert(UserDto source) {
        LibraryUser libraryUser = new LibraryUser();
        libraryUser.setId(source.id());
        libraryUser.setUsername(source.username());
        libraryUser.setEnabled(source.enabled());
        libraryUser.setRoles(source.roles());

        return libraryUser;
    }
}