package com.yoloo.server.user.domain.usecase

import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.user.domain.usecase.contract.ListFollowingsUseCaseContract

interface ListFollowingsUseCase : UseCase<ListFollowingsUseCaseContract.Request, ListFollowingsUseCaseContract.Response>