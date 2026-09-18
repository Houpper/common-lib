package br.com.houpper.common.exception.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import br.com.houpper.common.exception.feign.FeignErrorDecoder;
import feign.codec.ErrorDecoder;

/**
 * Autoconfiguração responsável por registrar o decoder de erros utilizado pelos clients Feign.
 */
@AutoConfiguration
@ConditionalOnClass(ErrorDecoder.class)
public class FeignAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }
}