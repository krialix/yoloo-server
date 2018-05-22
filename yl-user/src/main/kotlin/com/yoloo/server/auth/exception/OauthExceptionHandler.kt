package com.yoloo.server.auth.exception

import com.yoloo.server.api.exception.ForbiddenException
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class OauthExceptionHandler {

    @ExceptionHandler(AccessDeniedException::class)
    fun handleServiceExceptions(ex: AccessDeniedException, request: WebRequest): ResponseEntity<Any> {
        throw ForbiddenException("Access is denied")
    }
}