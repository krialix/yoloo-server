package com.yoloo.server.comment.infrastructure.mapper

import com.yoloo.server.comment.domain.entity.Comment
import com.yoloo.server.comment.domain.response.CommentResponse
import com.yoloo.server.common.response.attachment.SimpleAttachmentResponse
import com.yoloo.server.common.util.Mapper
import com.yoloo.server.post.domain.response.AuthorResponse
import org.springframework.stereotype.Component

@Component
class CommentResponseMapper : Mapper<Comment, CommentResponse> {

    override fun apply(from: Comment, payload: MutableMap<String, Any>): CommentResponse {
        return CommentResponse(
            id = from.id,
            author = AuthorResponse(
                id = from.author.id,
                displayName = from.author.displayName,
                url = from.author.url,
                image = SimpleAttachmentResponse(from.author.avatarUrl),
                self = from.author.self
            ),
            content = from.content.value,
            approved = from.approved,
            likeCount = from.likeCount,
            createdAt = from.createdAt
        )
    }
}