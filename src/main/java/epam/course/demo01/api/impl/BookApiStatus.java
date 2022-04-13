package epam.course.demo01.api.impl;

import epam.course.demo01.api.ApiStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BookApiStatus implements ApiStatus {

    NO_SUCH_BOOK(404001, "No such book found");

    private final Integer code;
    private final String description;

    @Override
    public Integer getCode() {
        return this.code;
    }

}
