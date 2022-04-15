package epam.course.demo01.api.impl;

import epam.course.demo01.api.ApiStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BookApiStatus implements ApiStatus {

    NO_SUCH_BOOK(404001, "No such book found"),
    BOOK_DELETION(404002, "No such book found for deletion");

    private final Integer code;
    private final String description;

    @Override
    public Integer getCode() {
        return this.code;
    }

}
