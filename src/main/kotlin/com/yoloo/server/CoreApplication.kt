package com.yoloo.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication
class CoreApplication : SpringBootServletInitializer()

fun main(args: Array<String>) {
    runApplication<CoreApplication>(*args)
}
