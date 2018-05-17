package com.yoloo.server.auth.api

import com.yoloo.server.auth.usecase.EmailValidationUseCase
import com.yoloo.server.auth.usecase.SignUpEmailUseCase
import com.yoloo.server.auth.vo.SignInFacebookRequest
import com.yoloo.server.auth.vo.SignInGoogleRequest
import com.yoloo.server.auth.vo.SignUpEmailRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

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
    fun signUp(@RequestBody @Valid @NotNull request: SignUpEmailRequest?): OAuth2AccessToken {
        return signUpEmailUseCase.execute(request!!)
    }

    @PostMapping("/signInGoogle")
    @ResponseStatus(HttpStatus.CREATED)
    fun signIn(@RequestBody @Valid @NotNull request: SignInGoogleRequest?): OAuth2AccessToken {
        TODO()
    }

    @PostMapping("/signInFacebook")
    @ResponseStatus(HttpStatus.CREATED)
    fun signIn(@RequestBody @Valid @NotNull request: SignInFacebookRequest?): OAuth2AccessToken {
        TODO()
    }
}