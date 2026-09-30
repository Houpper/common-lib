package br.com.houpper.common.exception.configuration;

import br.com.houpper.common.exception.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionAutoConfigurationTest {

    private final WebApplicationContextRunner contextRunner =
            new WebApplicationContextRunner()
                    .withConfiguration(
                            AutoConfigurations.of(
                                    ExceptionAutoConfiguration.class
                            )
                    );

    @Test
    void shouldRegisterGlobalExceptionHandlerInServletApplication() {

        contextRunner.run(context -> {

            assertThat(
                    context.getBeansOfType(
                            GlobalExceptionHandler.class
                    )
            ).hasSize(1);
        });
    }

    @Test
    void shouldNotReplaceExistingGlobalExceptionHandler() {

        GlobalExceptionHandler existingHandler = new GlobalExceptionHandler();

        contextRunner
                .withBean(
                        GlobalExceptionHandler.class,
                        () -> existingHandler
                )
                .run(context -> {

                    assertThat(
                            context.getBeansOfType(
                                    GlobalExceptionHandler.class
                            )
                    ).hasSize(1);

                    assertThat(
                            context.getBean(
                                    GlobalExceptionHandler.class
                            )
                    ).isSameAs(existingHandler);
                });
    }
}