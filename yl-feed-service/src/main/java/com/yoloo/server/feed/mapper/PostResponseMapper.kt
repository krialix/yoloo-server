package com.yoloo.server.feed.mapper

import com.yoloo.server.feed.jpa.Post
import com.yoloo.server.feed.vo.*
import org.springframework.stereotype.Component

@Component
class PostResponseMapper {

    fun apply(from: Post, self: Boolean, voted: Boolean, bookmarked: Boolean): PostResponse {
        return PostResponse(
            id = from.id,
            author = AuthorResponse(
                id = from.author.id,
                displayName = from.author.displayName,
                profileImageUrl = from.author.profileImageUrl,
                self = self
            ),
            title = from.title,
            content = from.content,
            group = PostGroupResponse(from.group.id, from.group.displayName),
            tags = from.tags.toList(),
            approvedCommentId = from.approvedCommentId,
            bounty = from.bounty,
            count = PostCountResponse(0, 0),
            voted = voted,
            bookmarked = bookmarked,
            createdAt = from.createdAt,
            medias = from.medias.map { MediaResponse(it.type.name, it.url.value) }
        )
    }
}