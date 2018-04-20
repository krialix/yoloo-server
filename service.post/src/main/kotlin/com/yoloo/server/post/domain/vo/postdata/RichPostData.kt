package com.yoloo.server.post.domain.vo.postdata

import com.googlecode.objectify.annotation.Subclass
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.domain.vo.*

@Subclass
@NoArg
open class RichPostData(
    override var title: PostTitle,

    override var topic: PostTopic,

    override var tags: Set<@JvmSuppressWildcards PostTag>,

    override var approvedCommentId: AcceptedCommentId? = null,

    override var bounty: PostBounty? = null,

    open var attachments: List<@JvmSuppressWildcards PostAttachment>
) : TextPostData(title, topic, tags, approvedCommentId, bounty)