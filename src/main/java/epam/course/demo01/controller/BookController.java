package epam.course.demo01.controller;

import epam.course.demo01.api.ApiResponse;
import epam.course.demo01.dto.BookDto;
import epam.course.demo01.entity.Book;
import epam.course.demo01.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

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
        log.info(" -> Book creation request: {}", book);
        Book newBook = bookService.createBook(book);
        log.info(" <- Book created with id[{}]", newBook.getId());
        return new BookDto(newBook);
    }

    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable @Min(1) Long id,
                              @Valid @RequestBody BookDto book,
                              @AuthenticationPrincipal OAuth2User user)
    {
        log.info(" -> Update request for Book[id={}] from [{}]. Update with {}",
                id, user.getAttribute("name"), book);
        Book changedBook = bookService.updateBook(id, book);
        log.info(" <- Book[id={}] updated: {}", id, changedBook);
        return new BookDto(changedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable @Min(1) Long id,
                                                 @AuthenticationPrincipal OAuth2User user)
    {
        log.info(" -> Delete request for Book[id={}] from [{}]", id, user.getAttribute("name"));
        if (bookService.deleteBook(id)) {
            log.info(" -> Book[id={}] deleted", id);
        }
        else {
            log.info(" <- Book[id={}] - no content", id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public Page<BookDto> getBookPage(Pageable pageable)
    {
        log.info(" -> Book page request: page = {}, size = {}, sort = {}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        Page<Book> bookPage = bookService.getBookPage(pageable);
        log.info(" <- Book page retrieved. Element count: {}", bookPage.getSize());
        return bookPage.map(converter::convert);
    }

    @GetMapping("/{id}")
    public BookDto getBook(@PathVariable @Min(1) Long id,
                           @AuthenticationPrincipal OAuth2User oauth2User)
    {
        log.info(" -> Book request id[{}]", id);
        Book book = bookService.getBook(id);
        log.info(" <- Book with id[{}] is retrieved", id);
        return new BookDto(book);
    }

}
