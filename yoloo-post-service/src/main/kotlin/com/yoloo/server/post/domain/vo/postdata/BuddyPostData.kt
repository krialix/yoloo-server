package com.yoloo.server.post.domain.vo.postdata

import com.googlecode.objectify.annotation.Subclass
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.domain.vo.*

@Subclass
@NoArg
data class BuddyPostData(
    override var title: PostTitle,

    override var topic: PostTopic,

    override var tags: Set<@JvmSuppressWildcards PostTag>,

    override var approvedCommentId: AcceptedCommentId? = null,

    override var bounty: PostBounty? = null,

    override var attachments: List<@JvmSuppressWildcards PostAttachment> = emptyList(),

    var buddyRequestInfo: BuddyRequestInfo
) : RichPostData(title, topic, tags, approvedCommentId, bounty, attachments)