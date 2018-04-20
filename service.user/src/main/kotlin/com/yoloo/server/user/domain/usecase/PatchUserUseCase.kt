package com.yoloo.server.user.domain.usecase

import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.user.domain.usecase.contract.PatchUserUseCaseContract

interface PatchUserUseCase : UseCase<PatchUserUseCaseContract.Request, Unit>