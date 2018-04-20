package com.yoloo.server.user.infrastructure.exception

import com.yoloo.server.common.api.exception.ConflictException

class UserAlreadyExistsException : ConflictException("users-1")