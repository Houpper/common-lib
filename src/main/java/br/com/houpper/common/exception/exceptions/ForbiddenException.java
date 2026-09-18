package br.com.houpper.common.exception.exceptions;

import org.springframework.security.access.AccessDeniedException;

/**
 * Exceção lançada quando um usuário autenticado tenta acessar um recurso para o qual não possui permissão.
 *
 * <p> É utilizada para indicar uma falha de autorização, resultando em uma resposta HTTP 403 (Forbidden) por meio do
 * {@code AccessDeniedHandler} configurado. </p>
 */
public class ForbiddenException extends AccessDeniedException {

    /**
     * Cria uma nova instância de {@code ForbiddenException}.
     *
     * @param message mensagem contendo o detalhe da falha de autorização.
     */
    public ForbiddenException(String message) {
        super(message);
    }

    /**
     * Cria uma nova instância de {@code ForbiddenException}.
     *
     * @param message mensagem contendo o detalhe da falha de autorização.
     * @param cause   exceção que originou a falha.
     */
    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}