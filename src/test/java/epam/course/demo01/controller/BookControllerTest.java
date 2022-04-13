package epam.course.demo01.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.course.demo01.dto.BookDto;
import epam.course.demo01.entity.Book;
import epam.course.demo01.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.testSecurityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("dev")
@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Test
    @WithMockUser
    @DisplayName("When given valid BookDto input object, perform HTTP POST on /books, then return 200 OK")
    void whenValidInput_thenReturn200ok() throws Exception {

        BookDto bookDto = new BookDto();
        bookDto.setName("Test book");
        bookDto.setReleaseDate(LocalDate.of(1995, 7, 3));

        Book newBook = new Book();
        newBook.setId(1L);
        newBook.setName("Test Book");
        newBook.setReleaseDate(LocalDate.now());

        when(bookService.createBook(any(BookDto.class))).thenReturn(newBook);

        mockMvc.perform(post("/books")
                .with(testSecurityContext())
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isOk());

    }

}