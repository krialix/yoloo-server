package com.yoloo.server.post.mapper

import com.yoloo.server.common.response.attachment.SimpleAttachmentResponse
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.vo.*
import com.yoloo.server.post.vo.postdataresponse.PostDataResponse
import com.yoloo.server.post.vo.postdataresponse.RichPostDataResponse
import com.yoloo.server.post.vo.postdataresponse.SponsoredPostDataResponse
import com.yoloo.server.post.vo.postdataresponse.TextPostDataResponse
import org.springframework.stereotype.Component

@Component
class PostResponseMapper {

    fun apply(from: Post, voted: Boolean = false): PostResponse {
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
            data = when (from.type) {
                PostType.TEXT -> mapToTextPostDataResponse(from, voted)
                PostType.ATTACHMENT -> mapToRichPostDataResponse(from, voted)
                PostType.SPONSORED -> mapToSponsoredPostDataResponse(from)
                PostType.BUDDY -> TODO()
            }
        )
    }

    private fun mapToTextPostDataResponse(post: Post, voted: Boolean): PostDataResponse {
        return TextPostDataResponse(
            title = post.title.value,
            group = PostGroupResponse(post.group.id, post.group.displayName),
            tags = post.tags.map { it.value },
            approvedCommentId = post.approvedCommentId?.value,
            bounty = post.coin?.value ?: 0,
            count = PostCountResponse(post.countData.voteCount, post.countData.commentCount),
            voted = voted,
            createdAt = post.createdAt
        )
    }

    private fun mapToRichPostDataResponse(post: Post, voted: Boolean): PostDataResponse {
        return RichPostDataResponse(
            title = post.title.value,
            group = PostGroupResponse(post.group.id, post.group.displayName),
            tags = post.tags.map { it.value },
            approvedCommentId = post.approvedCommentId?.value,
            bounty = post.coin?.value ?: 0,
            count = PostCountResponse(post.countData.voteCount, post.countData.commentCount),
            voted = voted,
            createdAt = post.createdAt,
            attachments = post.attachments.map { PostAttachmentResponse(it.url) }
        )
    }

    private fun mapToSponsoredPostDataResponse(post: Post): PostDataResponse {
        return SponsoredPostDataResponse(
            title = post.title.value,
            attachments = post.attachments.map { PostAttachmentResponse(it.url) }
        )
    }
}