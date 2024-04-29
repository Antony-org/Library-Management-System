package edu.com.librarymanagementsystem.patron.converter;

import edu.com.librarymanagementsystem.patron.Patron;
import edu.com.librarymanagementsystem.patron.dto.PatronDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PatronDtoToPatronConverter implements Converter<PatronDTO, Patron> {
    @Override
    public Patron convert(PatronDTO source) {
        Patron patron = new Patron();
        patron.setId(source.id());
        patron.setEmail(source.email());
        patron.setPhoneNumber(source.phoneNumber());
        patron.setName(source.name());

        return patron;
    }
}
