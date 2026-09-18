package br.com.houpper.common.exception.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando ocorre uma falha na comunicação ou no consumo de um serviço externo.
 *
 * <p> Retorna HTTP 502 (Bad Gateway), indicando que a aplicação recebeu uma resposta inválida ou não conseguiu obter
 * uma resposta adequada de um sistema externo necessário para processar a requisição. </p>
 */
public class ExternalServiceException extends BusinessException {

    /**
     * Título padrão da exceção.
     */
    private static final String TITULO = "Error consuming external service";

    /**
     * HttpStatus da exceção.
     */
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_GATEWAY;

    /**
     * Cria uma nova instância de {@code ExternalServiceException}.
     *
     * @param message Mensagem contendo o detalhe da exceção.
     */
    public ExternalServiceException(String message) {
        super(TITULO, HTTP_STATUS, message);
    }
}