package epam.course.demo01.converter;

import epam.course.demo01.dto.BookDto;
import epam.course.demo01.entity.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BookToBookDtoConverterTest {

    private static Converter<Book, BookDto> converter;

    @BeforeAll
    static void setUp() {
        converter = new BookToBookDtoConverter();
    }

    @Test
    @DisplayName("When given Book entity object, then convert it to BookDto object")
    void convert() {

        Book book = Book.builder()
                .id(1L)
                .name("Test Book")
                .releaseDate(LocalDate.now())
                .build();

        BookDto bookDto = converter.convert(book);

        assertAll(() -> {
            assertEquals(book.getId(), bookDto.getId());
            assertEquals(book.getName(), bookDto.getName());
            assertEquals(book.getReleaseDate(), bookDto.getReleaseDate());
        });

    }

}