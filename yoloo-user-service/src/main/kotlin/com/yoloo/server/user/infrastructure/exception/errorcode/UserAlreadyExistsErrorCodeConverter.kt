package com.yoloo.server.user.infrastructure.exception.errorcode

import com.yoloo.server.common.errorhandler.ErrorCode
import com.yoloo.server.common.errorhandler.ErrorCodeConverter
import com.yoloo.server.user.infrastructure.exception.UserAlreadyExistsException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class UserAlreadyExistsErrorCodeConverter : ErrorCodeConverter {

    override fun canHandle(exception: Exception?): Boolean {
        return exception is UserAlreadyExistsException
    }

    override fun toErrorCode(exception: Exception?): ErrorCode {
        return ErrorCode.of("users-1", HttpStatus.CONFLICT)
    }
}