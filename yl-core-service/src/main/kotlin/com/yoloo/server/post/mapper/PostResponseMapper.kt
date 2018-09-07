package com.yoloo.server.post.mapper

import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.vo.*
import org.springframework.stereotype.Component

@Component
class PostResponseMapper {

    fun apply(from: Post, self: Boolean, voted: Boolean, bookmarked: Boolean): PostResponse {
        return PostResponse(
            id = from.id,
            author = AuthorResponse(
                id = from.author.id,
                displayName = from.author.displayName,
                profileImageUrl = from.author.profileImageUrl.value,
                self = self
            ),
            title = from.title.value,
            content = from.content.value,
            group = PostGroupResponse(from.group.id, from.group.displayName),
            tags = from.tags.toList(),
            approvedCommentId = from.approvedCommentId?.value,
            bounty = from.bounty?.value ?: 0,
            count = PostCountResponse(from.countData.voteCount, from.countData.commentCount),
            voted = voted,
            bookmarked = bookmarked,
            createdAt = from.auditData.createdAt,
            medias = from.medias.map { MediaResponse(it.type.name, it.url.value) }
        )
    }
}
