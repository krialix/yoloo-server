package com.yoloo.server.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zalando.problem.ProblemModule
import org.zalando.problem.violations.ConstraintViolationProblemModule

@Configuration
class JacksonConfiguration {

    /*
     * Module for serialization/deserialization of RFC7807 Problem.
     */
    @Bean
    fun problemModule(): ProblemModule {
        return ProblemModule()
    }

    /*
     * Module for serialization/deserialization of ConstraintViolationProblem.
     */
    @Bean
    fun constraintViolationProblemModule(): ConstraintViolationProblemModule {
        return ConstraintViolationProblemModule()
    }
}
