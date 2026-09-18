package br.com.houpper.common.exception.model;

import lombok.Builder;

import java.io.Serializable;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Objeto para representar os detalhes de uma exceção retornada pela API para {@code @Valid}.
 *
 * <p> Implementa o padrão Problem Details for HTTP APIs (RFC 7807), fornecendo informações padronizadas sobre erros
 * ocorridos na requisição. </p>
 *
 * @param type      Identificador do tipo do erro.
 * @param title     Título resumido do erro.
 * @param status    Código HTTP da resposta.
 * @param detail    Descrição detalhada do erro.
 * @param errorCode Código interno do erro.
 * @param traceId   Identificador de rastreamento da requisição.
 * @param instance  Caminho ou recurso da requisição.
 * @param timestamp Data e hora da ocorrência do erro.
 * @param fields    Mapa contendo os campos inválidos e suas respectivas mensagens.
 */
@Builder
public record ValidationProblemDetails(

        URI type,

        String title,

        Integer status,

        String detail,

        String errorCode,

        String traceId,

        String instance,

        LocalDateTime timestamp,

        Map<String, String> fields

) implements Serializable {
}