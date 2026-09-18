package br.com.houpper.common.exception.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando um serviço externo está temporariamente indisponível.
 *
 * <p> Retorna HTTP 503 (Service Unavailable) quando uma dependência externa necessária para o processamento da
 * requisição não está disponível ou não pode ser acessada temporariamente. </p>
 */
public class ServiceUnavailableException extends BusinessException {

    /**
     * Título padrão da exceção.
     */
    private static final String TITLE = "Service Unavailable Exception";

    /**
     * HttpStatus da exceção.
     */
    private static final HttpStatus HTTP_STATUS = HttpStatus.SERVICE_UNAVAILABLE;

    /**
     * Cria uma nova instância de {@code ServiceUnavailableException}.
     *
     * @param message Mensagem contendo o detalhe da exceção.
     */
    public ServiceUnavailableException(String message) {
        super(TITLE, HTTP_STATUS, message);
    }
}