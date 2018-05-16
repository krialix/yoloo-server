package com.yoloo.server.auth.api

import com.yoloo.server.auth.usecase.EmailValidationUseCase
import com.yoloo.server.auth.usecase.SignUpEmailUseCase
import com.yoloo.server.auth.vo.SignUpEmailRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping(
    "/api/v1/m/auth",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
internal class AuthControllerV1(
    private val emailValidationUseCase: EmailValidationUseCase,
    private val signUpEmailUseCase: SignUpEmailUseCase
) {
    @PostMapping("/checkEmail")
    fun checkEmail(@RequestBody @Valid @NotBlank email: String?) {
        emailValidationUseCase.execute(email!!)
    }

    @PostMapping("/signUpEmail")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUpEmail(@RequestBody @Valid request: SignUpEmailRequest): OAuth2AccessToken {
        return signUpEmailUseCase.execute(SignUpEmailUseCase.Params(request))
    }

    @PostMapping("/signInGoogle")
    @ResponseStatus(HttpStatus.CREATED)
    fun signInGoogle(@RequestBody @Valid request: SignUpEmailRequest): OAuth2AccessToken {
        return signUpEmailUseCase.execute(SignUpEmailUseCase.Params(request))
    }

    @PostMapping("/signInFacebook")
    @ResponseStatus(HttpStatus.CREATED)
    fun signInFacebook(@RequestBody @Valid request: SignUpEmailRequest): OAuth2AccessToken {
        return signUpEmailUseCase.execute(SignUpEmailUseCase.Params(request))
    }
}