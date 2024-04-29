package edu.com.librarymanagementsystem.borrowingRecords.converter;

import edu.com.librarymanagementsystem.borrowingRecords.BorrowingRecord;
import edu.com.librarymanagementsystem.borrowingRecords.dto.BorrowingDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BorrowingToBorrowingDtoConverter  implements Converter<BorrowingRecord, BorrowingDto> {
    @Override
    public BorrowingDto convert(BorrowingRecord source) {
        return new BorrowingDto(source.getId(), source.getBook().getTitle(), source.getPatron().getName(),
                source.getBorrowingDate(), source.getReturnDate());
    }
}
