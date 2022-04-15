package epam.course.demo01.controller;

import epam.course.demo01.api.ApiResponse;
import epam.course.demo01.api.impl.BookApiHeader;
import epam.course.demo01.api.impl.BookApiStatus;
import epam.course.demo01.dto.BookDto;
import epam.course.demo01.entity.Book;
import epam.course.demo01.exception.BookDeletionException;
import epam.course.demo01.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.ZonedDateTime;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final Converter<Book, BookDto> converter;

    @PostMapping
    public BookDto createBook(@Valid @RequestBody BookDto book) {
        Book newBook = bookService.createBook(book);
        return new BookDto(newBook);
    }

    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable @Min(1) Long id,
                              @Valid @RequestBody BookDto book)
    {
        Book changedBook = bookService.updateBook(id, book);
        return new BookDto(changedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable @Min(1) Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public Page<BookDto> getBookPage(Pageable pageable) {
        Page<Book> bookPage = bookService.getBookPage(pageable);
        return bookPage.map(converter::convert);
    }

    @GetMapping("/{id}")
    public BookDto getBook(@PathVariable @Min(1) Long id) {
        Book book = bookService.getBook(id);
        return new BookDto(book);
    }

    @ExceptionHandler(BookDeletionException.class)
    public ResponseEntity<ApiResponse> handleBookDeletion(BookDeletionException e) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(BookApiHeader.BOOK_DELETION_HEADER.getHeader(), BookApiHeader.BOOK_DELETION_HEADER.getHeaderValue());

        ApiResponse response = ApiResponse.builder()
                .code(BookApiStatus.BOOK_DELETION.getCode())
                .message(e.getLocalizedMessage())
                .timestamp(ZonedDateTime.now())
                .headers(headers)
                .build();

        return new ResponseEntity<>(response, response.getHeaders(), HttpStatus.I_AM_A_TEAPOT);
    }

}
