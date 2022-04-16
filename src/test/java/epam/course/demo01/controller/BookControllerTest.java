package epam.course.demo01.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.course.demo01.api.ApiResponse;
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
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.testSecurityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    @DisplayName("When given valid BookDto object, perform HTTP POST on /books, then return 200 OK")
    void whenValidInput_thenReturn200ok() throws Exception {

        BookDto bookDto = new BookDto();
        bookDto.setName("Test book");
        bookDto.setReleaseDate(LocalDate.of(1995, 7, 3));

        Book newBook = Book.builder()
                .id(1L)
                .name("Test Book")
                .releaseDate(LocalDate.now())
                .build();

        when(bookService.createBook(any(BookDto.class))).thenReturn(newBook);

        mockMvc.perform(post("/books")
                .with(testSecurityContext()).with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    @DisplayName("When given invalid BookDto object, perform HTTP POST on /books, then return 418 I_AM_A_TEAPOT with custom headers and body")
    void whenInvalidInput_thenReturn418_I_AM_A_TEAPOT() throws Exception {

        BookDto bookDto = new BookDto();
        bookDto.setName("");
        bookDto.setReleaseDate(LocalDate.of(2077, 1, 1));

        ApiResponse apiResponse = ApiResponse.builder()
                .code(400002)
                .message("Exception occurred during method argument binding")
                .errors(Map.of(
                        "name", "Имя должно быть не короче 1 символа",
                        "releaseDate", "Дата выходы должна быть из прошлого"
                ))
                .build();

        String responseString = objectMapper.writeValueAsString(apiResponse);

        mockMvc.perform(post("/books")
                .with(testSecurityContext()).with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isIAmATeapot())
                .andExpect(header().stringValues(
                        "Method-Argument-Not-Valid",
                        "Exception occurred during method argument binding"
                ))
                .andExpect(content().json(responseString));

    }

}