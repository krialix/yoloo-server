package com.yoloo.server.post.mapper

import com.yoloo.server.common.vo.attachment.SimpleAttachmentResponse
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.vo.*
import com.yoloo.server.post.vo.postdataresponse.PostDataResponse
import com.yoloo.server.post.vo.postdataresponse.RichPostDataResponse
import com.yoloo.server.post.vo.postdataresponse.SponsoredPostDataResponse
import com.yoloo.server.post.vo.postdataresponse.TextPostDataResponse
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class PostResponseMapper {

    fun apply(from: Post, self: Boolean, voted: Boolean, bookmarked: Boolean): PostResponse {
        return PostResponse(
            id = from.id,
            type = from.type.name.toLowerCase(),
            author = AuthorResponse(
                id = from.author.id,
                displayName = from.author.displayName,
                image = SimpleAttachmentResponse(from.author.avatar.url.value),
                self = self,
                verified = from.author.verified
            ),
            content = from.content.value,
            data = when (from.type) {
                PostType.TEXT -> mapToTextPostDataResponse(from, voted, bookmarked)
                PostType.ATTACHMENT -> mapToRichPostDataResponse(from, voted, bookmarked)
                PostType.SPONSORED -> mapToSponsoredPostDataResponse(from)
                PostType.BUDDY -> TODO()
            }
        )
    }

    private fun mapToTextPostDataResponse(
        post: Post,
        voted: Boolean,
        bookmarked: Boolean
    ): PostDataResponse {
        return TextPostDataResponse(
            title = post.title.value,
            group = PostGroupResponse(post.group.id, post.group.displayName),
            tags = post.tags.map { it.value },
            approvedCommentId = post.approvedCommentId?.value,
            coin = post.coin?.value ?: 0,
            count = PostCountResponse(post.countData.voteCount, post.countData.commentCount),
            voted = voted,
            bookmarked = bookmarked,
            createdAt = post.auditData.createdAt
        )
    }

    private fun mapToRichPostDataResponse(
        post: Post,
        voted: Boolean,
        bookmarked: Boolean
    ): PostDataResponse {
        return RichPostDataResponse(
            title = post.title.value,
            group = PostGroupResponse(post.group.id, post.group.displayName),
            tags = post.tags.map { it.value },
            approvedCommentId = post.approvedCommentId?.value,
            coin = post.coin?.value ?: 0,
            count = PostCountResponse(post.countData.voteCount, post.countData.commentCount),
            voted = voted,
            bookmarked = bookmarked,
            createdAt = post.auditData.createdAt,
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