package com.yoloo.server.post.mapper

import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.vo.CommentResponse
import com.yoloo.server.post.vo.AuthorResponse

class CommentResponseMapper {

    fun apply(from: Comment, self: Boolean, voted: Boolean): CommentResponse {
        return CommentResponse(
            id = from.id,
            author = AuthorResponse(
                id = from.author.id,
                self = self,
                displayName = from.author.displayName,
                profileImageUrl = from.author.profileImageUrl.value
            ),
            content = from.content.value,
            approved = from.approved,
            voted = voted,
            voteCount = from.voteCount,
            createdAt = from.createdAt
        )
    }
}
