package br.com.houpper.common.exception.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Classe base para exceções de negócio personalizadas da aplicação.
 */
@Getter
public abstract class BusinessException extends RuntimeException {

    /**
     * Título da exceção.
     */
    private final String title;

    /**
     * Status HTTP da exceção.
     */
    private final HttpStatus httpStatus;

    /**
     * Cria uma nova instância de {@code BasicException}.
     *
     * @param title      Título da exceção.
     * @param httpStatus Http Status da exception.
     * @param message    Mensagem contendo o detalhe da exceção.
     */
    public BusinessException(String title, HttpStatus httpStatus, String message) {
        super(message);
        this.title = title;
        this.httpStatus = httpStatus;
    }
}