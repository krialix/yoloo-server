package com.yoloo.server.user.domain.usecase

import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.user.domain.usecase.contract.PatchUserContract

interface PatchUserUseCase : UseCase<PatchUserContract.Request, Unit>