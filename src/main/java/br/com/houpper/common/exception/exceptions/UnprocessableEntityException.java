package br.com.houpper.common.exception.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando a requisição é bem formada, mas não pode ser processada.
 *
 * <p> Retorna HTTP 422 (Unprocessable Entity) quando existem erros de validação ou regras de negócio impedem o
 * processamento da requisição. </p>
 */
public class UnprocessableEntityException extends BusinessException {

    /**
     * Título padrão da exceção.
     */
    private static final String TITLE = "Unprocessable Entity";

    /**
     * HttpStatus da exceção.
     */
    private static final HttpStatus HTTP_STATUS = HttpStatus.UNPROCESSABLE_CONTENT;

    /**
     * Cria uma nova instância de {@code UnprocessableEntityException}.
     *
     * @param message Mensagem contendo o detalhe da exceção.
     */
    public UnprocessableEntityException(String message) {
        super(TITLE, HTTP_STATUS, message);
    }
}