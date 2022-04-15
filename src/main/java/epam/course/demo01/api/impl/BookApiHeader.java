package epam.course.demo01.api.impl;

import epam.course.demo01.api.ApiHeader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookApiHeader implements ApiHeader {

    CUSTOM_HEADER("Custom-Header", "Custom header value"),
    BOOK_DELETION_HEADER("Book-Deletion", "Exception occurred during book deletion attempt");

    private final String header;
    private final String headerValue;

}
