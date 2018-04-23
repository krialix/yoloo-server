package com.yoloo.server.comment.domain.usecase

import com.yoloo.server.comment.domain.usecase.contract.ListCommentsContract
import com.yoloo.server.common.usecase.UseCase

interface ListCommentsUseCase : UseCase<ListCommentsContract.Request, ListCommentsContract.Response>