package br.com.houpper.common.exception.feign;

import br.com.houpper.common.exception.exceptions.*;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FeignErrorDecoderTest {

    private final FeignErrorDecoder decoder = new FeignErrorDecoder(new ObjectMapper());

    @ParameterizedTest
    @MethodSource("mappedStatuses")
    void shouldMapHttpStatusToHoupperException(
            int status,
            Class<? extends Exception> expectedType) {

        Response response = response(
                status,
                """
                {
                    "detail": "Remote error"
                }
                """
        );

        Exception exception =
                decoder.decode("ExampleClient#get", response);

        assertInstanceOf(expectedType, exception);
        assertEquals("Remote error", exception.getMessage());
    }

    static Stream<Arguments> mappedStatuses() {
        return Stream.of(
                Arguments.of(400, BadRequestException.class),
                Arguments.of(401, UnauthorizedException.class),
                Arguments.of(403, ForbiddenException.class),
                Arguments.of(404, NotFoundException.class),
                Arguments.of(409, ConflictException.class),
                Arguments.of(422, UnprocessableEntityException.class),
                Arguments.of(500, InternalServerErrorException.class),
                Arguments.of(503, ServiceUnavailableException.class)
        );
    }

    private Response response(int status, String body) {
        return Response.builder()
                .status(status)
                .reason("Test error")
                .request(Request.create(
                        Request.HttpMethod.GET,
                        "http://localhost/test",
                        Map.of(),
                        null,
                        StandardCharsets.UTF_8,
                        null
                ))
                .headers(Map.of("Content-Type", List.of("application/json")))
                .body(body, StandardCharsets.UTF_8)
                .build();
    }

    @Test
    void shouldUseDefaultMessageWhenBodyIsNull() {
        Response response = response(500, null);

        Exception exception =
                decoder.decode("ExampleClient#get", response);

        assertInstanceOf(
                InternalServerErrorException.class,
                exception
        );

        assertEquals(
                "Remote service error",
                exception.getMessage()
        );
    }

    @Test
    void shouldUseDefaultMessageWhenJsonIsInvalid() {
        Response response =
                response(500, "{invalid-json");

        Exception exception =
                decoder.decode("ExampleClient#get", response);

        assertEquals(
                "Remote service error",
                exception.getMessage()
        );
    }

    @Test
    void shouldUseDefaultMessageWhenDetailDoesNotExist() {
        Response response =
                response(
                        400,
                        """
                        {
                            "title": "Bad Request"
                        }
                        """
                );

        Exception exception =
                decoder.decode("ExampleClient#get", response);

        assertEquals(
                "Remote service error",
                exception.getMessage()
        );
    }

    @Test
    void shouldDelegateUnknownStatusToDefaultDecoder() {
        Response response =
                response(418, "I'm a teapot");

        Exception exception =
                decoder.decode("ExampleClient#get", response);

        assertNotNull(exception);

        assertFalse(
                exception instanceof BusinessException
        );
    }
}