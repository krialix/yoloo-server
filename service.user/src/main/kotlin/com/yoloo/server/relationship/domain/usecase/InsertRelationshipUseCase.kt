package com.yoloo.server.relationship.domain.usecase

import com.yoloo.server.common.usecase.UseCase
import com.yoloo.server.relationship.domain.usecase.contract.InsertRelationshipContract

interface InsertRelationshipUseCase : UseCase<InsertRelationshipContract.Request, Unit>