package com.yoloo.server.post.domain.usecase

import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.post.domain.usecase.contract.GetPostUseCaseContract

interface GetPostUseCase : UseCase<GetPostUseCaseContract.Request, GetPostUseCaseContract.Response>