package br.com.houpper.common.exception.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando um ou mais campos da requisição possuem valores inválidos, não atendendo às regras básicas de
 * validação da aplicação.
 *
 * <p> Retorna HTTP 400 (Bad Request), indicando que a requisição não pode ser processada devido a dados inválidos
 * fornecidos pelo cliente. </p>
 */
public class InvalidFieldException extends BusinessException {

    /**
     * Título padrão da exceção.
     */
    private static final String TITULO = "Invalid value entered";

    /**
     * HttpStatus da exceção.
     */
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    /**
     * Cria uma nova instância de {@code InvalidFieldException}.
     *
     * @param message Mensagem contendo o detalhe da exceção.
     */
    public InvalidFieldException(String message) {
        super(TITULO, HTTP_STATUS, message);
    }
}