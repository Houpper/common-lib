package br.com.houpper.common.exception.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada para recurso não encontrado.
 *
 * <p> Retorna HTTP 404 (Not Found) quando o recurso solicitado não existe ou não pôde ser localizado. </p>
 */
public class ResourceNotFoundException extends BusinessException {

    /**
     * Título padrão da exceção.
     */
    private static final String TITULO = "Resource Not Found";

    /**
     * HttpStatus da exceção.
     */
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

    /**
     * Cria uma nova exceção {@code ResourceNotFoundException}.
     *
     * @param message Mensagem contendo o detalhe da exceção.
     */
    public ResourceNotFoundException(String message) {
        super(TITULO, HTTP_STATUS, message);
    }
}