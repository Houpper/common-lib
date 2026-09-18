package br.com.houpper.common.exception.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando ocorre conflito na requisição.
 *
 * <p> Retorna HTTP 409 (Conflict) quando a operação não pode ser concluída devido a um conflito com o
 * estado atual do recurso (ex: duplicidade de dados). </p>
 */
public class ConflictException extends BusinessException {

    /**
     * Título padrão da exceção.
     */
    private static final String TITLE = "Conflict";

    /**
     * HttpStatus da exceção.
     */
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;

    /**
     * Cria uma nova instância de {@code ConflictException}.
     *
     * @param message Mensagem contendo o detalhe da exceção.
     */
    public ConflictException(String message) {
        super(TITLE, HTTP_STATUS, message);
    }
}