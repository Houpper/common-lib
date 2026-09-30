package br.com.houpper.common.exception.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Exceção lançada quando ocorre uma falha no processo de autenticação da aplicação.
 *
 * <p> É utilizada para indicar que o usuário ou tenant não pôde ser autenticado, resultando em uma resposta HTTP 401
 * (Unauthorized) por meio do {@code AuthenticationEntryPoint} configurado. </p>
 */
public class UnauthorizedException extends AuthenticationException {

    /**
     * Cria uma nova instância de {@code UnauthorizedException}.
     *
     * @param message Mensagem contendo o detalhe da falha de autenticação.
     */
    public UnauthorizedException(String message) {
        super(message);
    }

    /**
     * Cria uma nova instância de {@code UnauthorizedException}.
     *
     * @param message Mensagem contendo o detalhe da falha de autenticação.
     * @param cause   Exceção que originou a falha.
     */
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}