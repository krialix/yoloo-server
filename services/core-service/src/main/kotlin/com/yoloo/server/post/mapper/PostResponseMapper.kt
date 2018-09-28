package com.yoloo.server.post.mapper

import com.arcticicestudio.icecore.hashids.Hashids
import com.yoloo.server.mapper.ResponseMapperWithParams
import com.yoloo.server.mapper.ResponseParams
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.vo.MediaResponse
import com.yoloo.server.post.vo.PostCountResponse
import com.yoloo.server.post.vo.PostGroupResponse
import com.yoloo.server.post.vo.PostResponse
import org.springframework.stereotype.Component

@Component
class PostResponseMapper(
    private val hashIds: Hashids,
    private val authorResponseMapper: AuthorResponseMapper
) : ResponseMapperWithParams<Post, PostResponse, PostResponseMapper.Params> {

    override fun apply(t: Post, u: Params): PostResponse {
        return PostResponse(
            id = hashIds.encode(t.id, t.author.id),
            author = authorResponseMapper.apply(t.author, AuthorResponseMapper.Params(u.self)),
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
