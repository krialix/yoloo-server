package com.yoloo.server.user.domain.usecase

import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.user.domain.usecase.contract.ListFollowersContract

interface ListFollowersUseCase : UseCase<ListFollowersContract.Request, ListFollowersContract.Response>