package br.com.houpper.common.exception.handler;

import br.com.houpper.common.exception.exceptions.BadRequestException;
import br.com.houpper.common.exception.model.ProblemDetails;
import br.com.houpper.common.exception.model.ValidationProblemDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void shouldHandleBusinessException() {

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/api/users");

        BadRequestException exception =
                new BadRequestException(
                        "Invalid request"
                );

        ResponseEntity<ProblemDetails> response =
                handler.handleBasicException(
                        exception,
                        request
                );

        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                "Bad Request Exception",
                response.getBody().title()
        );

        assertEquals(
                400,
                response.getBody().status()
        );

        assertEquals(
                "Invalid request",
                response.getBody().detail()
        );

        assertEquals(
                "BAD_REQUEST",
                response.getBody().errorCode()
        );

        assertEquals(
                "/api/users",
                response.getBody().instance()
        );

        assertNotNull(
                response.getBody().traceId()
        );

        assertNotNull(
                response.getBody().timestamp()
        );
    }

    @Test
    void shouldHandleValidationException() {

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/api/users");

        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(
                        new Object(),
                        "request"
                );

        bindingResult.addError(
                new FieldError(
                        "request",
                        "name",
                        "must not be blank"
                )
        );

        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(
                        null,
                        bindingResult
                );

        ResponseEntity<ValidationProblemDetails> response =
                handler.handleMethodArgumentNotValid(
                        exception,
                        request
                );

        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                "must not be blank",
                response.getBody()
                        .fields()
                        .get("name")
        );
    }

    @Test
    void shouldNotExposeUnexpectedExceptionMessage() {

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/api/test");

        ResponseEntity<ProblemDetails> response =
                handler.handleException(
                        new RuntimeException(
                                "database password=secret"
                        ),
                        request
                );

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                "An unexpected error occurred",
                response.getBody().detail()
        );

        assertFalse(
                response.getBody()
                        .detail()
                        .contains("password")
        );
    }
}