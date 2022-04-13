package epam.course.demo01.converter;

import epam.course.demo01.dto.BookDto;
import epam.course.demo01.entity.Book;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BookToBookDtoConverter implements Converter<Book, BookDto> {
    @Override
    public BookDto convert(Book source) {
        return new BookDto(source);
    }
}
