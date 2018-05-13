package com.yoloo.server.auth.application

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.yoloo.server.auth.domain.usecase.EmailValidationUseCase
import com.yoloo.server.auth.domain.usecase.InsertAccountUseCase
import com.yoloo.server.user.domain.requestpayload.InsertUserPayload
import com.yoloo.server.user.domain.response.UserResponse
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping(
    "/api/internal/mobile/v1/auth",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE],
    consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
internal class AuthControllerV1 @Autowired constructor(
    private val insertAccountUseCase: InsertAccountUseCase,
    private val emailValidationUseCase: EmailValidationUseCase
) {
    @PostMapping("/checkEmail")
    fun checkEmail(@RequestBody @Valid @NotBlank email: String?) {
        emailValidationUseCase.execute(email!!)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun insertUser(@RequestBody @Valid payload: InsertUserPayload): UserResponse {
        return insertAccountUseCase.execute(InsertAccountUseCase.Params(payload))
    }

    @GetMapping("/deneme")
    fun signInWithGoogle(): JSONObject {
        return JWT.create()
            .withSubject("1234")
            .withExpiresAt(Date.from(Instant.now().plusMillis(864_000_000)))
            .withIssuedAt(Date())
            .sign(Algorithm.none())
            .let { JSONObject(it) }
    }
}