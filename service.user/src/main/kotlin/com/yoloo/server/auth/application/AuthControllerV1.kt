package com.yoloo.server.auth.application

import com.yoloo.server.auth.domain.usecase.EmailValidationUseCase
import com.yoloo.server.auth.domain.usecase.InsertAccountUseCase
import com.yoloo.server.user.domain.requestpayload.InsertUserPayload
import com.yoloo.server.user.domain.response.UserResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping(
    "/api/v1/auth",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE],
    consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
internal class AuthControllerV1 @Autowired constructor(
    private val insertAccountUseCase: InsertAccountUseCase,
    private val emailValidationUseCase: EmailValidationUseCase
) {
    @GetMapping("/self")
    fun getSelf(authentication: Authentication?): Principal? {
        return authentication
    }

    @PostMapping("/checkEmail")
    fun checkEmail(@RequestBody @Valid @NotBlank email: String?) {
        emailValidationUseCase.execute(email!!)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun insertUser(@RequestBody @Valid payload: InsertUserPayload): UserResponse {
        return insertAccountUseCase.execute(InsertAccountUseCase.Request(payload))
    }
}