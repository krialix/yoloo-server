package com.yoloo.server.user.entity

import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.yoloo.server.common.util.NoArg

@NoArg
@Entity
data class BookmarkBucket(@Id var id: Long, var ids: List<Long>)