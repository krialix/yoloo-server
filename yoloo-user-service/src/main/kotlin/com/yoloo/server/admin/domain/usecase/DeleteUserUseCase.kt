package com.yoloo.server.admin.domain.usecase

import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.admin.domain.usecase.contract.DeleteUserUseCaseContract

interface DeleteUserUseCase : UseCase<DeleteUserUseCaseContract.Request, Unit>