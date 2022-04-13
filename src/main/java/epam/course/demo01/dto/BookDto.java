package epam.course.demo01.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import epam.course.demo01.entity.Book;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class BookDto {

    private Long id;

    @NotNull(message = "Имя не может быть пустым")
    @Size(min = 1, message = "Имя должно быть не короче 1 символа")
    private String name;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @NotNull(message = "Дата выхода не должна быть пустой")
    @Past(message = "Дата выходы должна быть из прошлого")
    private LocalDate releaseDate;

    public BookDto(Book book) {
        this.id = book.getId();
        this.name = book.getName();
        this.releaseDate = book.getReleaseDate();
    }

}
