package com.yoloo.server.post.vo.postdata

import com.googlecode.objectify.annotation.Subclass
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.vo.*

@Subclass
@NoArg
data class BuddyPostData(
    override var title: PostTitle,

    override var group: PostGroup,

    override var tags: Set<@JvmSuppressWildcards PostTag>,

    override var approvedCommentId: AcceptedCommentId? = null,

    override var bounty: PostBounty? = null,

    override var attachments: List<@JvmSuppressWildcards PostAttachment> = emptyList(),

    var buddyRequestInfo: BuddyRequestInfo
) : RichPostData(title, group, tags, approvedCommentId, bounty, attachments)