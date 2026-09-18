package br.com.houpper.common.exception.handler;

import br.com.houpper.common.exception.exceptions.BusinessException;
import br.com.houpper.common.exception.model.ProblemDetails;
import br.com.houpper.common.exception.model.ValidationProblemDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Handler global responsável por interceptar e padronizar as respostas de erro da aplicação.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * URI padrão utilizada nas respostas de erro para identificar o tipo do problema seguindo o padrão RFC 7807.
     */
    private static final URI DEFAULT_URL_TYPE = URI.create("about:blank");

    /**
     * Trata exceções de negócio baseadas em {@link BusinessException}.
     *
     * @param ex      Exceção lançada.
     * @param request Requisição Http.
     * @return Resposta padronizada do erro.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ProblemDetails> handleBasicException(
            BusinessException ex,
            HttpServletRequest request) {

        ProblemDetails problem = ProblemDetails.builder()
                .type(DEFAULT_URL_TYPE)
                .title(ex.getTitle())
                .status(ex.getHttpStatus().value())
                .detail(ex.getMessage())
                .errorCode(ex.getHttpStatus().name())
                .traceId(UUID.randomUUID().toString())
                .instance(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(problem);
    }

    /**
     * Trata erros de validação para {@code @Valid} de campos enviados na requisição.
     *
     * @param ex      Exceção lançada da validação.
     * @param request Requisição Http.
     * @return Resposta padronizada contendo os campos inválidos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationProblemDetails> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> fields = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        fields.put(error.getField(), error.getDefaultMessage()));

        ValidationProblemDetails problem = ValidationProblemDetails.builder()
                .type(DEFAULT_URL_TYPE)
                .title("Field validation error")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail("Field validation error")
                .errorCode(HttpStatus.BAD_REQUEST.name())
                .traceId(UUID.randomUUID().toString())
                .instance(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .fields(fields)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    /**
     * Trata exceções inesperadas não mapeadas por handlers específicos.
     *
     * @param ex      Exceção lançada durante o processamento da requisição.
     * @param request Requisição HTTP
     * @return Resposta padronizada de erro interno
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetails> handleException(Exception ex, HttpServletRequest request) {

        if (ex instanceof ErrorResponse errorResponse) {

            ProblemDetail body = errorResponse.getBody();

            ProblemDetails problem = new ProblemDetails(
                    DEFAULT_URL_TYPE,
                    body.getTitle(),
                    body.getStatus(),
                    ex.getMessage(),
                    HttpStatus.valueOf(body.getStatus()).name(),
                    UUID.randomUUID().toString(),
                    request.getRequestURI(),
                    LocalDateTime.now()
            );
            return ResponseEntity.status(errorResponse.getStatusCode()).body(problem);
        }

        ProblemDetails problem = new ProblemDetails(
                DEFAULT_URL_TYPE,
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                UUID.randomUUID().toString(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.internalServerError().body(problem);
    }
}