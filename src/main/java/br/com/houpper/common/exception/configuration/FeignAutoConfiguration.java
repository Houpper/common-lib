package br.com.houpper.common.exception.configuration;

import br.com.houpper.common.exception.feign.FeignErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ObjectMapper;

/**
 * Autoconfiguração responsável por registrar o decoder de erros utilizado pelos clients Feign.
 */
@AutoConfiguration
@ConditionalOnClass({
        ErrorDecoder.class,
        ObjectMapper.class,
        FeignErrorDecoder.class
})
public class FeignAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ErrorDecoder.class)
    ErrorDecoder errorDecoder(ObjectMapper objectMapper) {
        return new FeignErrorDecoder(objectMapper);
    }
}