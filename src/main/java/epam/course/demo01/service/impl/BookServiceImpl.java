package epam.course.demo01.service.impl;

import epam.course.demo01.dto.BookDto;
import epam.course.demo01.entity.Book;
import epam.course.demo01.exception.NoSuchBookException;
import epam.course.demo01.repository.BookRepository;
import epam.course.demo01.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.info(" <- No such book with id[{}]", id);
                    return new NoSuchBookException(id);
                });
    }

    public Page<Book> getBookPage(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book updateBook(Long id, BookDto book) {
        Book bookToUpdate = this.getBook(id);
        bookToUpdate.setName(book.getName());
        bookToUpdate.setReleaseDate(book.getReleaseDate());

        return bookRepository.save(bookToUpdate);
    }

    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }

        return false;
    }

    public Book createBook(@Valid BookDto bookDto) {
        Book book = this.toBook(bookDto);
        return bookRepository.save(book);
    }

    private Book toBook(@Valid BookDto bookDto) {
        Book book = new Book();
        book.setName(bookDto.getName());
        book.setReleaseDate(bookDto.getReleaseDate());

        return book;
    }

}
