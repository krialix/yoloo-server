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
import com.yoloo.server.post.domain.vo.postdata.RichPostData
import com.yoloo.server.post.domain.vo.postdata.SponsoredPostData
import com.yoloo.server.post.domain.vo.postdata.TextPostData
import org.springframework.stereotype.Component

@Component
class PostResponseMapper : Mapper<Post, PostResponse> {

    override fun apply(from: Post, payload: MutableMap<String, Any>): PostResponse {
        val dataResponse = when (from.type) {
            PostType.TEXT -> mapToTextPostDataResponse(from, payload)
            PostType.ATTACHMENT -> mapToRichPostDataResponse(from, payload)
            PostType.SPONSORED -> mapToSponsoredPostDataResponse(from)
            PostType.BUDDY -> TODO()
        }

        return PostResponse(
            id = from.key.toWebSafeString(),
            type = from.type.name.toLowerCase(),
            author = AuthorResponse(
                id = from.author.id,
                displayName = from.author.displayName,
                image = SimpleAttachmentResponse(from.author.avatar.url.value),
                self = from.author.self
            ),
            content = from.content.value,
            data = dataResponse
        )
    }

    private fun mapToTextPostDataResponse(from: Post, payload: MutableMap<String, Any>): PostDataResponse {
        val data = from.data as TextPostData
        return TextPostDataResponse(
            title = data.title.value,
            group = PostGroupResponse(id = data.group.id, displayName = data.group.displayName),
            tags = data.tags.map { it.value },
            approvedCommentId = data.approvedCommentId?.value,
            bounty = data.bounty?.value ?: 0,
            count = PostCountResponse(
                payload.getOrDefault("voteCount", 0) as Int,
                payload.getOrDefault("commentCount", 0) as Int
            ),
            voteDir = payload.getOrDefault("voteDir", 0) as Int,
            createdAt = from.createdAt
        )
    }

    private fun mapToRichPostDataResponse(from: Post, payload: MutableMap<String, Any>): PostDataResponse {
        val data = from.data as RichPostData
        return RichPostDataResponse(
            title = data.title.value,
            group = PostGroupResponse(id = data.group.id, displayName = data.group.displayName),
            tags = data.tags.map { it.value },
            approvedCommentId = data.approvedCommentId?.value,
            bounty = data.bounty?.value ?: 0,
            count = PostCountResponse(
                payload.getOrDefault("voteCount", 0) as Int,
                payload.getOrDefault("commentCount", 0) as Int
            ),
            voteDir = payload.getOrDefault("voteDir", 0) as Int,
            createdAt = from.createdAt,
            attachments = data.attachments.map { PostAttachmentResponse(it.url) }
        )
    }

    private fun mapToSponsoredPostDataResponse(from: Post): PostDataResponse {
        val data = from.data as SponsoredPostData
        return SponsoredPostDataResponse(
            title = data.title.value,
            attachments = data.attachments?.map { PostAttachmentResponse(it.url) } ?: emptyList()
        )
    }
}