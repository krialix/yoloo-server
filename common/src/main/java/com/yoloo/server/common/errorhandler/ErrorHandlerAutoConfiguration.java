package com.yoloo.server.common.errorhandler;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorHandlerAutoConfiguration {

    @Bean
    public ErrorCodeFactory errorCodeFactory(ApplicationContext applicationContext) {
        return new ErrorCodeFactory(applicationContext);
    }

    @Bean
    public ApiExceptionHandler apiExceptionHandler(ErrorCodeFactory errorCodeFactory, MessageSource messageSource) {
        return new ApiExceptionHandler(errorCodeFactory, messageSource);
    }
}
