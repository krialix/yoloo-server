package com.yoloo.server.post.infrastructure.mapper

import com.yoloo.server.common.response.attachment.SimpleAttachmentResponse
import com.yoloo.server.common.util.Mapper
import com.yoloo.server.post.domain.entity.Post
import com.yoloo.server.post.domain.response.*
import org.springframework.stereotype.Component

@Component
class PostResponseMapper : Mapper<Post, PostResponse> {

    override fun apply(from: Post, payload: MutableMap<String, Any>): PostResponse {
        return PostResponse(
            id = from.id,
            type = from.type.name.toLowerCase(),
            author = AuthorResponse(
                id = from.author.id,
                displayName = from.author.displayName,
                url = from.author.url,
                image = SimpleAttachmentResponse(from.author.avatarUrl),
                self = from.author.self
            ),
            approvedCommentId = from.approvedCommentId?.value,
            topic = PostTopicResponse(id = from.topic.topicId, displayName = from.topic.displayName),
            bounty = from.bounty?.value ?: 0,
            tags = from.tags.map { it.value },
            title = from.title.value,
            content = from.content.value,
            attachments = from.attachments?.map { PostAttachmentResponse(it.url) } ?: emptyList(),
            count = PostCountResponse(payload["voteCount"] as Int, payload["commentCount"] as Int),
            voteDir = payload["voteDir"] as Int,
            createdAt = from.createdAt
        )
    }
}