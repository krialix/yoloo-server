package com.yoloo.server.admin.domain.usecase

import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.admin.domain.usecase.contract.DeleteUserContract

interface DeleteUserUseCase : UseCase<DeleteUserContract.Request, Unit>