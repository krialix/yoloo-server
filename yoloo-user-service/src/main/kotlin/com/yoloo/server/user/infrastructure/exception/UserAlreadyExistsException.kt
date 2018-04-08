package com.yoloo.server.user.infrastructure.exception

import com.yoloo.server.common.api.exception.ServiceException
import org.springframework.http.HttpStatus

class UserAlreadyExistsException : ServiceException("users-1", HttpStatus.CONFLICT)