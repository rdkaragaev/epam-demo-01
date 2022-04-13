package epam.course.demo01.controller.advice;

import epam.course.demo01.api.ApiResponse;
import epam.course.demo01.api.impl.BookApiHeader;
import epam.course.demo01.api.impl.BookApiStatus;
import epam.course.demo01.exception.NoSuchBookException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(NoSuchBookException.class)
    public ResponseEntity<ApiResponse> handleNoSuchBook(NoSuchBookException ex) {
        HttpHeaders headers = new HttpHeaders();
        String anotherHeaderValue = "Another value for this header";
        headers.addAll(BookApiHeader.CUSTOM_HEADER.getHeader(), List.of(BookApiHeader.CUSTOM_HEADER.getHeaderValue(), anotherHeaderValue));

        ApiResponse response = ApiResponse.builder()
                .code(BookApiStatus.NO_SUCH_BOOK.getCode())
                .message(ex.getLocalizedMessage())
                .timestamp(ZonedDateTime.now())
                .headers(headers)
                .build();

        return new ResponseEntity<>(response, response.getHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolation(ConstraintViolationException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Constraint-Violation", "Constraint violation occurred");

        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        ApiResponse response = ApiResponse.builder()
                .code(400001)
                .message("Constraint violation occurred")
                .headers(headers)
                .errors(errors)
                .timestamp(ZonedDateTime.now())
                .build();

        return new ResponseEntity<>(response, response.getHeaders(), HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Method-Argument-Not-Valid", "Exception occurred during method argument binding");

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.put(error.getObjectName(), error.getDefaultMessage());
        }

        ApiResponse response = ApiResponse.builder()
                .code(400002)
                .message("Exception occurred during method argument binding")
                .headers(headers)
                .errors(errors)
                .timestamp(ZonedDateTime.now())
                .build();

        return new ResponseEntity<>(response, response.getHeaders(), HttpStatus.I_AM_A_TEAPOT);
    }

}
