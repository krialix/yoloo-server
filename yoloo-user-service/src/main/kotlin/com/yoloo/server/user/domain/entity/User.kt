package com.yoloo.server.user.domain.entity

import com.fasterxml.uuid.Generators
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.OnSave
import com.yoloo.server.common.mixins.Keyable
import com.yoloo.server.common.mixins.Validatable
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.user.domain.vo.UserCountData
import com.yoloo.server.user.domain.vo.UserFilterData
import com.yoloo.server.user.domain.vo.UserPrimaryData
import com.yoloo.server.user.domain.vo.UserSecondaryData
import javax.validation.Valid

@NoArg
@Entity
data class User constructor(
    @Id
    var id: String = Generators.timeBasedGenerator().generate().toString().replace("-", ""),

    @field:Valid
    var userPrimaryData: UserPrimaryData,

    @field:Valid
    var userSecondaryData: UserSecondaryData,

    @field:Valid
    var countData: UserCountData = UserCountData(),

    var userFilterData: UserFilterData = UserFilterData()
) : Validatable, Keyable<User> {

    @OnSave
    override fun validate() {
        super.validate()
    }
}