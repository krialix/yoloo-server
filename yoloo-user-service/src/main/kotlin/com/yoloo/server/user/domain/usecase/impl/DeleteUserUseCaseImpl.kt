package com.yoloo.server.user.domain.usecase.impl

import com.yoloo.server.user.domain.usecase.DeleteUserUseCase
import com.yoloo.server.user.domain.usecase.contract.DeleteUserUseCaseContract
import org.springframework.stereotype.Service

@Service
class DeleteUserUseCaseImpl : DeleteUserUseCase {

    override fun execute(request: DeleteUserUseCaseContract.Request) {
        // TODO implement delete logic
    }
}