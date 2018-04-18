package com.yoloo.server.post.infrastructure.mapper

import com.yoloo.server.common.response.attachment.SimpleAttachmentResponse
import com.yoloo.server.common.util.Mapper
import com.yoloo.server.post.domain.entity.Post
import com.yoloo.server.post.domain.response.*
import com.yoloo.server.post.domain.response.postdata.PostDataResponse
import com.yoloo.server.post.domain.response.postdata.RichPostDataResponse
import com.yoloo.server.post.domain.response.postdata.SponsoredPostDataResponse
import com.yoloo.server.post.domain.response.postdata.TextPostDataResponse
import com.yoloo.server.post.domain.vo.PostType
import org.springframework.stereotype.Component

@Component
class PostResponseMapper : Mapper<Post, PostResponse> {

    override fun apply(from: Post, payload: MutableMap<String, Any>): PostResponse {
        val postDataResponse = when (from.type) {
            PostType.TEXT -> mapToTextPostDataResponse(from, payload)
            PostType.ATTACHMENT -> mapToRichPostDataResponse(from, payload)
            PostType.SPONSORED -> mapToSponsoredPostDataResponse(from)
            PostType.BUDDY -> TODO()
        }

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
            data = postDataResponse
        )

        /*return PostResponseOld(
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
        )*/
    }

    private fun mapToTextPostDataResponse(from: Post, payload: MutableMap<String, Any>): PostDataResponse {
        return TextPostDataResponse(
            title = from.title.value,
            content = from.content.value,
            topic = PostTopicResponse(id = from.topic.topicId, displayName = from.topic.displayName),
            tags = from.tags.map { it.value },
            approvedCommentId = from.approvedCommentId?.value,
            bounty = from.bounty?.value ?: 0,
            count = PostCountResponse(
                payload.getOrDefault("voteCount", 0) as Int,
                payload.getOrDefault("commentCount", 0) as Int
            ),
            voteDir = payload.getOrDefault("voteDir", 0) as Int,
            createdAt = from.createdAt
        )
    }

    private fun mapToRichPostDataResponse(from: Post, payload: MutableMap<String, Any>): PostDataResponse {
        return RichPostDataResponse(
            title = from.title.value,
            content = from.content.value,
            topic = PostTopicResponse(id = from.topic.topicId, displayName = from.topic.displayName),
            tags = from.tags.map { it.value },
            approvedCommentId = from.approvedCommentId?.value,
            bounty = from.bounty?.value ?: 0,
            count = PostCountResponse(
                payload.getOrDefault("voteCount", 0) as Int,
                payload.getOrDefault("commentCount", 0) as Int
            ),
            voteDir = payload.getOrDefault("voteDir", 0) as Int,
            createdAt = from.createdAt,
            attachments = from.attachments?.map { PostAttachmentResponse(it.url) } ?: emptyList()
        )
    }

    private fun mapToSponsoredPostDataResponse(from: Post): PostDataResponse {
        return SponsoredPostDataResponse(
            title = from.title.value,
            content = from.content.value,
            attachments = from.attachments?.map { PostAttachmentResponse(it.url) } ?: emptyList()
        )
    }
}