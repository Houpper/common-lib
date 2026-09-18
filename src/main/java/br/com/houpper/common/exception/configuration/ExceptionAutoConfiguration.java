package br.com.houpper.common.exception.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import br.com.houpper.common.exception.handler.GlobalExceptionHandler;

/**
 * Autoconfiguração responsável por registrar o tratamento global de exceções da aplicação.
 */
@AutoConfiguration
public class ExceptionAutoConfiguration {

    @Bean
    GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}