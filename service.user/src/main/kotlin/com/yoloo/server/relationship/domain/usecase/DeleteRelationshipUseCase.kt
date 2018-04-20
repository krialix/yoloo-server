package com.yoloo.server.relationship.domain.usecase

import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.relationship.domain.usecase.contract.DeleteRelationshipUseCaseContract

interface DeleteRelationshipUseCase : UseCase<DeleteRelationshipUseCaseContract.Request, Unit>