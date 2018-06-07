package com.yoloo.server.user.api

import com.yoloo.server.user.usecase.CheckEmailAvailabilityUseCase
import com.yoloo.server.user.usecase.SignUpEmailUseCase
import com.yoloo.server.user.vo.CheckEmailAvailabilityRequest
import com.yoloo.server.user.vo.SignInFacebookRequest
import com.yoloo.server.user.vo.SignInGoogleRequest
import com.yoloo.server.user.vo.SignUpEmailRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@RestController
@RequestMapping(
    "/api/auth",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
class AuthController(
    private val checkEmailAvailabilityUseCase: CheckEmailAvailabilityUseCase,
    private val signUpEmailUseCase: SignUpEmailUseCase
) {

    @PostMapping("/checkEmail")
    fun checkEmail(@RequestBody @Valid @NotBlank request: CheckEmailAvailabilityRequest?) {
        checkEmailAvailabilityUseCase.execute(request!!)
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