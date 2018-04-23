package com.yoloo.server.user.domain.usecase

import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.user.domain.usecase.contract.InsertUserContract

interface InsertUserUseCase : UseCase<InsertUserContract.Request, InsertUserContract.Response>