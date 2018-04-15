package com.yoloo.server.user.domain.usecase

import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.user.domain.usecase.contract.DeleteRelationshipUseCaseContract

interface DeleteRelationshipUseCase : UseCase<DeleteRelationshipUseCaseContract.Request, Unit>