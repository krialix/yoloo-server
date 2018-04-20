package com.yoloo.server.post.domain.usecase

import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.post.domain.usecase.contract.ListTopicPostsContract

interface ListTopicPostsUseCase : UseCase<ListTopicPostsContract.Request, ListTopicPostsContract.Response>