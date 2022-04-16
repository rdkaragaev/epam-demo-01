package epam.course.demo01.service.impl;

import epam.course.demo01.dto.BookDto;
import epam.course.demo01.entity.Book;
import epam.course.demo01.exception.BookDeletionException;
import epam.course.demo01.exception.NoSuchBookException;
import epam.course.demo01.repository.BookRepository;
import epam.course.demo01.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No such book with id[{}]", id);
                    return new NoSuchBookException(id);
                });
    }

    public Page<Book> getBookPage(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book updateBook(Long id, BookDto book) {
        Book bookToUpdate = getBook(id);
        bookToUpdate.setName(book.getName());
        bookToUpdate.setReleaseDate(book.getReleaseDate());

        return bookRepository.save(bookToUpdate);
    }

    public void deleteBook(Long id) {
        try {
            bookRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e) {
            log.debug("No such book with id[{}] for deletion", id);
            throw new BookDeletionException(id);
        }
    }

    public Book createBook(BookDto bookDto) {
        Book book = toBook(bookDto);
        return bookRepository.save(book);
    }

    private Book toBook(BookDto bookDto) {
        return Book.builder()
                .name(bookDto.getName())
                .releaseDate(bookDto.getReleaseDate())
                .build();
    }

}
