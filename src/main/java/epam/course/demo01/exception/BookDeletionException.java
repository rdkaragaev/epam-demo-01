package epam.course.demo01.exception;

public class BookDeletionException extends RuntimeException {

    public BookDeletionException(Long id) {
        super("Не удалось найти книгу с id[" + id + "] для её удаления");
    }

}
