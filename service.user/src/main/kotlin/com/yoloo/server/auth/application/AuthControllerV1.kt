package com.yoloo.server.auth.application

import com.yoloo.server.auth.domain.request.SignUpEmailRequest
import com.yoloo.server.auth.domain.usecase.EmailValidationUseCase
import com.yoloo.server.auth.domain.usecase.SignUpEmailUseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping(
    "/api/v1/m/auth",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE],
    consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
internal class AuthControllerV1 @Autowired constructor(
    private val emailValidationUseCase: EmailValidationUseCase,
    private val signUpEmailUseCase: SignUpEmailUseCase
) {
    @PostMapping("/checkEmail")
    fun checkEmail(@RequestBody @Valid @NotBlank email: String?) {
        emailValidationUseCase.execute(email!!)
    }

    @PostMapping("/signUpEmail")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUpEmail(@RequestBody @Valid body: SignUpEmailRequest): OAuth2AccessToken {
        return signUpEmailUseCase.execute(SignUpEmailUseCase.Params(body))
    }
}