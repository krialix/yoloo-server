package com.yoloo.server.relationship.domain.usecase

import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.relationship.domain.usecase.contract.DeleteRelationshipContract

interface DeleteRelationshipUseCase : UseCase<DeleteRelationshipContract.Request, Unit>