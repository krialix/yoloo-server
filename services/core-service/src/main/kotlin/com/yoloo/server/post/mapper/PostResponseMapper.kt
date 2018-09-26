package com.yoloo.server.post.mapper

import com.arcticicestudio.icecore.hashids.Hashids
import com.yoloo.server.mapper.ResponseMapper
import com.yoloo.server.mapper.ResponseParams
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.vo.*
import org.springframework.stereotype.Component

@Component
class PostResponseMapper(
    private val hashIds: Hashids,
    private val authorResponseMapper: AuthorResponseMapper
) : ResponseMapper<Post, PostResponse, PostResponseMapper.Params> {

    override fun apply(t: Post, u: Params): PostResponse {
        return PostResponse(
            id = hashIds.encode(t.id, t.author.id),
            author = authorResponse2(),
            title = t.title.value,
            content = t.content.value,
            group = PostGroupResponse(hashIds.encode(t.group.id), t.group.displayName),
            tags = t.tags.toList(),
            approvedCommentId = t.approvedCommentId?.let { hashIds.encode(it.value) },
            bounty = t.bounty?.value ?: 0,
            count = PostCountResponse(t.countData.voteCount, t.countData.commentCount),
            liked = u.liked,
            bookmarked = u.bookmarked,
            createdAt = t.auditData.createdAt,
            medias = t.medias.map { MediaResponse(it.type.name, it.url.value) }
        )
    }

    data class Params(val self: Boolean, val liked: Boolean, val bookmarked: Boolean) : ResponseParams
}
