package com.yoloo.server.auth.application

import com.yoloo.server.auth.domain.usecase.EmailValidationUseCase
import com.yoloo.server.auth.domain.usecase.InsertUserUseCase
import com.yoloo.server.user.domain.requestpayload.InsertUserPayload
import com.yoloo.server.user.domain.response.UserResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@RestController
@ResponseBody
@RequestMapping(
    "/api/v1/auth",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE],
    consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
internal class AuthControllerV1 @Autowired constructor(
    private val insertUserUseCase: InsertUserUseCase,
    private val emailValidationUseCase: EmailValidationUseCase
) {

    @PostMapping("/checkEmail")
    @ResponseStatus(HttpStatus.OK)
    fun checkEmail(@RequestBody @Valid @NotBlank email: String?) {
        emailValidationUseCase.execute(email!!)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun insertUser(@RequestBody @Valid payload: InsertUserPayload): UserResponse {
        return insertUserUseCase.execute(InsertUserUseCase.Request(payload))
    }
}