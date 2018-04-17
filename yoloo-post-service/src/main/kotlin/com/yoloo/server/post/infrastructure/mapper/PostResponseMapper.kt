package com.yoloo.server.post.infrastructure.mapper

import com.yoloo.server.common.Mapper
import com.yoloo.server.common.response.attachment.SimpleAttachmentResponse
import com.yoloo.server.post.domain.entity.Post
import com.yoloo.server.post.domain.response.*
import org.springframework.stereotype.Component

@Component
class PostResponseMapper : Mapper<Post, PostResponse> {

    override fun apply(post: Post): PostResponse {
        return PostResponse(
            id = post.id,
            type = post.type.name.toLowerCase(),
            author = AuthorResponse(
                id = post.author.id,
                displayName = post.author.displayName,
                url = post.author.url,
                image = SimpleAttachmentResponse(post.author.avatarUrl),
                self = post.author.self
            ),
            approvedCommentId = post.approvedCommentId?.value,
            topic = PostTopicResponse(id = post.topic.topicId, displayName = post.topic.displayName),
            bounty = post.bounty?.value ?: 0,
            tags = post.tags.map { it.value },
            title = post.title.value,
            content = post.content.value,
            attachments = post.attachments?.map { PostAttachmentResponse(it.url) } ?: emptyList(),
            count = PostCountResponse(post.voteCount, post.commentCount),
            voteDir = post.voteDir,
            createdAt = post.createdAt
        )
    }
}