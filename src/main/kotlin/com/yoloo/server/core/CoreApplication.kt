package com.yoloo.server.core

import com.googlecode.objectify.ObjectifyFilter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
import javax.servlet.Filter

@ServletComponentScan
@SpringBootApplication
class CoreApplication : SpringBootServletInitializer() {

    override fun configure(builder: SpringApplicationBuilder): SpringApplicationBuilder {
        return builder.sources(CoreApplication::class.java)
    }

    @Bean
    fun objectifyFilter(): Filter {
        return ObjectifyFilter()
    }
}

fun main(args: Array<String>) {
    runApplication<CoreApplication>(*args)
}
