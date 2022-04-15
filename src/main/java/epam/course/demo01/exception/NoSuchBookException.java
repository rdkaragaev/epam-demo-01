package epam.course.demo01.exception;

public class NoSuchBookException extends RuntimeException {

    public NoSuchBookException(Long id) {
        super("Книги с таким id[" + id + "] не найдено");
    }

}
