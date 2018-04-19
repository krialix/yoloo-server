package com.yoloo.server.post.domain.vo.postdata

import com.googlecode.objectify.annotation.IgnoreSave
import com.googlecode.objectify.annotation.Index
import com.googlecode.objectify.annotation.Subclass
import com.googlecode.objectify.condition.IfNull
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.domain.vo.*
import javax.validation.Valid
import javax.validation.constraints.Size

@Subclass
@NoArg
open class TextPostData(
    @field:Valid
    open var title: PostTitle,

    @field:Valid
    open var topic: PostTopic,

    @field:Size(max = 10)
    @Index
    open var tags: Set<@JvmSuppressWildcards PostTag>,

    @IgnoreSave(IfNull::class)
    open var approvedCommentId: AcceptedCommentId? = null,

    @field:Valid
    open var bounty: PostBounty? = null
) : PostData()