package br.com.houpper.common.exception.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando o recurso solicitado não é encontrado.
 *
 * <p> Retorna HTTP 404 (Not Found) quando o recurso requisitado pelo cliente
 * não existe ou não pôde ser localizado. </p>
 */
public class NotFoundException extends BusinessException {

    /**
     * Título padrão da exceção.
     */
    private static final String TITLE = "Not Found Exception";

    /**
     * HttpStatus da exceção.
     */
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

    /**
     * Cria uma nova instância de {@code NotFoundException}.
     *
     * @param message Mensagem contendo o detalhe da exceção.
     */
    public NotFoundException(String message) {
        super(TITLE, HTTP_STATUS, message);
    }
}