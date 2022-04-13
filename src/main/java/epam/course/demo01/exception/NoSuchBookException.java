package epam.course.demo01.exception;

public class NoSuchBookException extends RuntimeException {

    public NoSuchBookException(Long id) {
        super("Книги с таким id[" + id + "] не найдено");
    }

    @Deprecated(forRemoval = true)
    public NoSuchBookException(String name) {
        super("Книги с названием [" + name + "] не найдено");
    }

}
