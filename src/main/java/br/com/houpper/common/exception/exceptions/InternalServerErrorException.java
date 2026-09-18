package br.com.houpper.common.exception.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando ocorre um erro interno inesperado no servidor.
 *
 * <p> Retorna HTTP 500 (Internal Server Error) quando a aplicação falha ao processar a requisição
 * devido a uma condição não tratada ou erro inesperado. </p>
 */
public class InternalServerErrorException extends BusinessException {

    /**
     * Título padrão da exceção.
     */
    private static final String TITLE = "Internal Server Error";

    /**
     * HttpStatus da exceção.
     */
    private static final HttpStatus HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    /**
     * Cria uma nova instância de {@code InternalServerErrorException}.
     *
     * @param message Mensagem contendo o detalhe da exceção.
     */
    public InternalServerErrorException(String message) {
        super(TITLE, HTTP_STATUS, message);
    }
}