package com.yoloo.server.post.mapper

import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.vo.CommentResponse
import com.yoloo.server.common.response.attachment.SimpleAttachmentResponse
import com.yoloo.server.post.vo.AuthorResponse
import org.springframework.stereotype.Component

@Component
class CommentResponseMapper {

    fun apply(from: Comment, self: Boolean, voted: Boolean): CommentResponse {
        return CommentResponse(
            id = from.id,
            author = AuthorResponse(
                id = from.author.id,
                displayName = from.author.displayName,
                image = SimpleAttachmentResponse(from.author.avatar.url.value),
                self = self,
                verified = from.author.verified
            ),
            content = from.content.value,
            approved = from.approved,
            voted = voted,
            voteCount = from.voteCount,
            createdAt = from.createdAt
        )
    }
}