package com.yoloo.server.comment.mapper

import com.yoloo.server.common.vo.attachment.SimpleAttachmentResponse
import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.post.vo.AuthorResponse
import com.yoloo.server.comment.vo.CommentResponse

class CommentResponseMapper {

    fun apply(from: Comment, self: Boolean, voted: Boolean): CommentResponse {
        return CommentResponse(
            id = from.id,
            author = AuthorResponse(
                id = from.author.id,
                self = self,
                displayName = from.author.displayName,
                image = SimpleAttachmentResponse(from.author.avatar.url.value)
            ),
            content = from.content.value,
            approved = from.approved,
            voted = voted,
            voteCount = from.voteCount,
            createdAt = from.auditData.createdAt
        )
    }
}