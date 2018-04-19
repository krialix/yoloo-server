package com.yoloo.server.post.domain.usecase

import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.post.domain.usecase.contract.GetPostContract

interface GetPostUseCase : UseCase<GetPostContract.Request, GetPostContract.Response>