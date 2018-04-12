package com.yoloo.server.user.domain.usecase.contract

interface DeleteUserUseCaseContract {

    data class Request(val userId: String)
}