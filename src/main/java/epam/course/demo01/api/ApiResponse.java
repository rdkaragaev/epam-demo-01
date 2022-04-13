package epam.course.demo01.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;

import java.time.ZonedDateTime;
import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    private final Integer code;
    private final String message;
    private final ZonedDateTime timestamp;
    private final Map<String, String> errors;

    @JsonIgnore
    private final HttpHeaders headers;

}
