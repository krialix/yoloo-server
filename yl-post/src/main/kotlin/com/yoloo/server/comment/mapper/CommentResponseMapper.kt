package com.yoloo.server.comment.mapper

import com.yoloo.server.comment.entity.Comment
import com.yoloo.server.comment.vo.CommentResponse
import com.yoloo.server.common.response.attachment.SimpleAttachmentResponse
import com.yoloo.server.common.util.Mapper
import com.yoloo.server.post.vo.AuthorResponse
import org.springframework.stereotype.Component

@Component
class CommentResponseMapper : Mapper<Comment, CommentResponse> {

    override fun apply(from: Comment, payload: MutableMap<String, Any>): CommentResponse {
        return CommentResponse(
            id = from.id,
            author = AuthorResponse(
                id = from.author.id,
                displayName = from.author.displayName,
                image = SimpleAttachmentResponse(from.author.avatar.url.value),
                self = from.author.self
            ),
            content = from.content.value,
            approved = from.approved,
            voteCount = from.likeCount,
            createdAt = from.createdAt
        )
    }
}