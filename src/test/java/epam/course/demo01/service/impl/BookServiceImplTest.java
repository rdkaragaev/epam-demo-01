package epam.course.demo01.service.impl;

import epam.course.demo01.converter.BookToBookDtoConverter;
import epam.course.demo01.dto.BookDto;
import epam.course.demo01.entity.Book;
import epam.course.demo01.exception.BookDeletionException;
import epam.course.demo01.exception.NoSuchBookException;
import epam.course.demo01.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Test
    @DisplayName("Given existing id for a Book entity, then return this Book entity")
    void getBookByExistingId() {
        Optional<Book> optionalBook = Optional.of(Book.builder()
                .id(1L)
                .name("Test Book")
                .releaseDate(LocalDate.of(1995, 7, 3))
                .build()
        );

        when(bookRepository.findById(1L)).thenReturn(optionalBook);

        Book book = bookService.getBook(1L);

        verify(bookRepository).findById(1L);

        assertAll(() -> {
            assertEquals(book.getId(), optionalBook.get().getId());
            assertEquals(book.getName(), optionalBook.get().getName());
            assertEquals(book.getReleaseDate(), optionalBook.get().getReleaseDate());
        });
    }

    @Test
    @DisplayName("Given non-existing id for a Book entity, then throw NoSuchBookException")
    void getBookByNonExistingId() {
        Throwable throwable = assertThrowsExactly(NoSuchBookException.class, () -> bookService.getBook(333L));

        assertEquals("Книги с таким id[333] не найдено", throwable.getMessage());
    }

    @Test
    @DisplayName("Given Long id and valid BookDto object, then update Book entity with the same id")
    void updateBook() {
        Optional<Book> bookToUpdate = Optional.of(Book.builder()
                .id(78L)
                .name("Entity Book")
                .releaseDate(LocalDate.now())
                .build()
        );

        BookDto bookDto = new BookDto();
        bookDto.setName("Updated Book");
        bookDto.setReleaseDate(LocalDate.of(1995, 7,3));

        when(bookRepository.findById(78L)).thenReturn(bookToUpdate);

        Book savedUpdatedBook = Book.builder()
                .id(78L)
                .name("Updated Book")
                .releaseDate(LocalDate.of(1995, 7,3))
                .build();

        doReturn(savedUpdatedBook).when(bookRepository).save(any());

        Book result = bookService.updateBook(78L, bookDto);

        assertAll(() -> {
            assertEquals(savedUpdatedBook.getId(), result.getId());
            assertEquals(savedUpdatedBook.getName(), result.getName());
            assertEquals(savedUpdatedBook.getReleaseDate(), result.getReleaseDate());
        });

    }

    @Test
    @DisplayName("Given existing id for a Book entity for its deletion, then verify deletion")
    void deleteBookByExistingId() {
        doNothing().when(bookRepository).deleteById(any());

        bookService.deleteBook(123L);

        verify(bookRepository).deleteById(123L);
    }

    @Test
    @DisplayName("Given non-existing id for a Book entity for its deletion, then throw BookDeletionException")
    void deleteBookByNonExistingId() {
        doThrow(EmptyResultDataAccessException.class).when(bookRepository).deleteById(any());

        Throwable throwable = assertThrowsExactly(BookDeletionException.class, () -> bookService.deleteBook(873L));

        assertEquals("Не удалось найти книгу с id[873] для её удаления", throwable.getMessage());
    }

}