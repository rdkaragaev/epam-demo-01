package epam.course.demo01.service;

import epam.course.demo01.dto.BookDto;
import epam.course.demo01.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

    Book getBook(Long id);

    Book createBook(BookDto bookDto);

    Page<Book> getBookPage(Pageable pageable);

    Book updateBook(Long id, BookDto book);

    void deleteBook(Long id);

}
