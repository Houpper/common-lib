package br.com.houpper.common.exception.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada para requisições inválidas.
 *
 * <p> Retorna HTTP 400 (Bad Request) quando a requisição enviada pelo cliente possui dados inválidos, inconsistentes ou
 * não atende aos requisitos necessários para o seu processamento. </p>
 */
public class BadRequestException extends BusinessException {

    /**
     * Título padrão da exceção.
     */
    private static final String TITLE = "Bad Request Exception";

    /**
     * HttpStatus da exceção.
     */
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    /**
     * Cria uma nova instância de {@code BadRequestException}.
     *
     * @param message Mensagem contendo o detalhe da exceção.
     */
    public BadRequestException(String message) {
        super(TITLE, HTTP_STATUS, message);
    }
}