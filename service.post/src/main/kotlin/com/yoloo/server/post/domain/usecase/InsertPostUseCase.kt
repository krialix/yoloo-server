package com.yoloo.server.post.domain.usecase

import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.post.domain.usecase.contract.InsertPostContract

interface InsertPostUseCase : UseCase<InsertPostContract.Request, InsertPostContract.Response>