package br.com.houpper.common.exception.feign;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import br.com.houpper.common.exception.exceptions.*;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

/**
 * Decoder de erros do Feign responsável por transformar respostas HTTP de erro de serviços externos
 * em exceções de domínio da aplicação.
 * <p>
 * Também tenta extrair a mensagem de erro do corpo da resposta (campo "detail"), retornando uma
 * mensagem padrão, caso não seja possível interpretar o conteúdo.
 * </p>
 */
public class FeignErrorDecoder implements ErrorDecoder {

    /**
     * Mensagem padrão utilizada quando não é possível extrair detalhes do erro.
     */
    private static final String DEFAULT_MESSAGE_ERROR = "Remote service error";

    /**
     * Decoder padrão do Feign utilizado como fallback.
     */
    private final ErrorDecoder defaultErrorDecoder = new Default();

    /**
     * Mapper utilizado para leitura do JSON retornado pelo serviço remoto.
     */
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {

        String message = extractMessage(response);

        return switch (response.status()) {

            case 400 -> new BadRequestException(message);

            case 401 -> new UnauthorizedException(message);

            case 403 -> new ForbiddenException(message);

            case 404 -> new NotFoundException(message);

            case 409 -> new ConflictException(message);

            case 422 -> new UnprocessableEntityException(message);

            case 500 -> new InternalServerErrorException(message);

            case 503 -> new ServiceUnavailableException(message);

            default -> defaultErrorDecoder.decode(methodKey, response);
        };
    }

    /**
     * Extrai a mensagem de erro a partir do corpo da resposta HTTP.
     * <p>
     * Caso o corpo contenha um JSON com o campo "detail", esse valor será retornado.
     * Caso contrário, retorna o corpo bruto ou uma mensagem padrão.
     * </p>
     *
     * @param response Resposta HTTP recebida do serviço remoto
     * @return Mensagem de erro interpretada ou padrão
     */
    private String extractMessage(Response response) {

        if (response.body() == null) {
            return DEFAULT_MESSAGE_ERROR;
        }

        try {

            String body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));

            JsonNode json = mapper.readTree(body);

            String detail = json.path("detail").asString();

            return detail != null && !detail.isBlank() ? detail : body;

        } catch (IOException ex) {
            return DEFAULT_MESSAGE_ERROR;
        }
    }
}