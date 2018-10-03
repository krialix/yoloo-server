package com.yoloo.server.post.mapper

import com.arcticicestudio.icecore.hashids.Hashids
import com.yoloo.server.mapper.ResponseMapper
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.vo.CommentResponse
import org.springframework.stereotype.Component

@Component
class CommentResponseMapper(
    private val hashids: Hashids,
    private val authorResponseMapper: AuthorResponseMapper
) : ResponseMapper<Comment, CommentResponse> {

    override fun apply(t: Comment): CommentResponse {
        return CommentResponse(
            id = hashids.encode(t.id, t.author.id, t.postId),
            author = authorResponseMapper.apply(t.author),
            content = t.content.value,
            approved = t.approved,
            liked = t.liked,
            likeCount = t.likes,
            createdAt = t.createdAt
        )
    }
}
