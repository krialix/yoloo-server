package com.yoloo.server.user.domain.usecase

import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.user.domain.usecase.contract.ListFollowingsContract

interface ListFollowingsUseCase : UseCase<ListFollowingsContract.Request, ListFollowingsContract.Response>