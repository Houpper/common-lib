package br.com.houpper.common.exception.feign;

import br.com.houpper.common.exception.exceptions.BadRequestException;
import br.com.houpper.common.exception.exceptions.ConflictException;
import br.com.houpper.common.exception.exceptions.ForbiddenException;
import br.com.houpper.common.exception.exceptions.InternalServerErrorException;
import br.com.houpper.common.exception.exceptions.NotFoundException;
import br.com.houpper.common.exception.exceptions.ServiceUnavailableException;
import br.com.houpper.common.exception.exceptions.UnauthorizedException;
import br.com.houpper.common.exception.exceptions.UnprocessableEntityException;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Decoder de erros do Feign responsável por transformar respostas HTTP de erro dos serviços remotos em exceções
 * padronizadas da aplicação.
 *
 * <p> Para respostas contendo um corpo JSON no padrão da plataforma Houpper, utiliza o campo {@code detail} como
 * mensagem da exceção. </p>
 */
public class FeignErrorDecoder implements ErrorDecoder {

    /**
     * Mensagem padrão utilizada quando não é possível extrair detalhes da resposta do serviço remoto.
     */
    private static final String DEFAULT_ERROR_MESSAGE = "Remote service error";

    /**
     * Decoder padrão do Feign utilizado para códigos HTTP não tratados especificamente por este decoder.
     */
    private static final ErrorDecoder DEFAULT_ERROR_DECODER = new ErrorDecoder.Default();

    /**
     * Mapper configurado pela aplicação.
     */
    private final ObjectMapper mapper;

    /**
     * Cria uma nova instância do decoder.
     *
     * @param mapper ObjectMapper configurado pela aplicação.
     */
    public FeignErrorDecoder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Decodifica uma resposta HTTP de erro.
     *
     * @param methodKey Identificação do método Feign.
     * @param response Resposta HTTP recebida do serviço remoto.
     * @return exceção Correspondente ao status HTTP.
     */
    @Override
    public Exception decode(String methodKey, Response response) {

        return switch (response.status()) {

            case 400 -> new BadRequestException(extractMessage(response));

            case 401 -> new UnauthorizedException(extractMessage(response));

            case 403 -> new ForbiddenException(extractMessage(response));

            case 404 -> new NotFoundException(extractMessage(response));

            case 409 -> new ConflictException(extractMessage(response));

            case 422 -> new UnprocessableEntityException(extractMessage(response));

            case 500 -> new InternalServerErrorException(extractMessage(response));

            case 503 -> new ServiceUnavailableException(extractMessage(response));

            default -> DEFAULT_ERROR_DECODER.decode(methodKey, response);
        };
    }

    /**
     * Extrai a mensagem de erro do corpo da resposta.
     *
     * <p> Para respostas JSON, utiliza o campo {@code detail}. Caso o corpo esteja ausente, vazio ou não contenha um
     * campo {@code detail} válido, retorna uma mensagem padrão. </p>
     *
     * @param response resposta HTTP recebida do serviço remoto.
     * @return mensagem do erro.
     */
    private String extractMessage(Response response) {

        if (response.body() == null) {
            return DEFAULT_ERROR_MESSAGE;
        }

        try {

            String body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));

            if (body == null || body.isBlank()) {
                return DEFAULT_ERROR_MESSAGE;
            }

            JsonNode json = mapper.readTree(body);

            if (json == null || !json.isObject()) {
                return DEFAULT_ERROR_MESSAGE;
            }

            JsonNode detail = json.get("detail");

            if (detail != null && detail.isString() && !detail.asString().isBlank()) {
                return detail.asString();
            }

        } catch (StreamReadException ignored) {
            // JSON inválido: utiliza mensagem padrão.
        } catch (IOException ignored) {
            // Resposta inválida: utiliza mensagem padrão.
        }

        return DEFAULT_ERROR_MESSAGE;
    }
}