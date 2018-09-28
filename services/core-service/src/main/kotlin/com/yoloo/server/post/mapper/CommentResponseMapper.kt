package com.yoloo.server.post.mapper

import com.arcticicestudio.icecore.hashids.Hashids
import com.yoloo.server.mapper.ResponseMapperWithParams
import com.yoloo.server.post.entity.Comment
import com.yoloo.server.post.vo.CommentResponse

class CommentResponseMapper(
    private val hashids: Hashids,
    private val authorResponseMapper: AuthorResponseMapper
) : ResponseMapperWithParams<Comment, CommentResponse, CommentResponseMapper.Params> {

    override fun apply(t: Comment, u: Params): CommentResponse {
        return CommentResponse(
            id = hashids.encode(t.id, t.author.id, t.postId.value),
            author = authorResponseMapper.apply(t.author, AuthorResponseMapper.Params(u.self)),
            content = t.content.value,
            approved = t.approved,
            liked = u.liked,
            likeCount = 0,
            createdAt = t.createdAt
        )
    }

    data class Params(val self: Boolean, val liked: Boolean)
}
