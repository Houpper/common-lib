package br.com.houpper.common.exception.configuration;

import br.com.houpper.common.exception.feign.FeignErrorDecoder;
import feign.codec.ErrorDecoder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

class FeignAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withConfiguration(
                            AutoConfigurations.of(
                                    FeignAutoConfiguration.class
                            )
                    );

    @Test
    void shouldRegisterErrorDecoder() {

        contextRunner
                .withBean(
                        ObjectMapper.class,
                        ObjectMapper::new
                )
                .run(context -> assertThat(
                        context.getBean(ErrorDecoder.class)
                )
                        .isInstanceOf(FeignErrorDecoder.class));
    }

    @Test
    void shouldNotReplaceCustomErrorDecoder() {

        contextRunner
                .withUserConfiguration(
                        CustomErrorDecoderConfiguration.class
                )
                .withBean(
                        ObjectMapper.class,
                        ObjectMapper::new
                )
                .run(context -> {

                    ErrorDecoder decoder =
                            context.getBean(ErrorDecoder.class);

                    assertThat(decoder)
                            .isSameAs(
                                    CustomErrorDecoderConfiguration.CUSTOM_DECODER
                            );

                    assertThat(
                            context.getBeansOfType(
                                    FeignErrorDecoder.class
                            )
                    ).isEmpty();
                });
    }

    @Configuration(proxyBeanMethods = false)
    static class CustomErrorDecoderConfiguration {

        static final ErrorDecoder CUSTOM_DECODER =
                (methodKey, response) ->
                        new RuntimeException();

        @Bean
        ErrorDecoder customErrorDecoder() {
            return CUSTOM_DECODER;
        }
    }
}