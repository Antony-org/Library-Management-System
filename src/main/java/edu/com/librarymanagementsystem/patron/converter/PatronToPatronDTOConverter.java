package edu.com.librarymanagementsystem.patron.converter;

import edu.com.librarymanagementsystem.patron.Patron;
import edu.com.librarymanagementsystem.patron.dto.PatronDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PatronToPatronDTOConverter implements Converter<Patron, PatronDTO> {
    @Override
    public PatronDTO convert(Patron source) {
        PatronDTO patronDTO = new PatronDTO(source.getId(), source.getName(), source.getEmail(),
                source.getPhoneNumber(),source.getNumberOfBorrowedRecords());
        return patronDTO;
    }
}
