package com.yoloo.server.post.domain.response

import com.fasterxml.jackson.annotation.JsonIgnore
import com.yoloo.server.post.domain.entity.Post
import org.dialectic.jsonapi.ResourceLinkage
import org.dialectic.jsonapi.relationship.Relationship
import org.dialectic.jsonapi.relationship.Relationships
import org.dialectic.jsonapi.resource.Resource
import java.time.LocalDateTime

data class PostResponse(
    @JsonIgnore
    val id: String,

    val type: String,

    @JsonIgnore
    val ownerId: String,

    val title: String,

    val content: String,

    val topic: PostTopicResponse,

    val tags: List<String>,

    val approvedCommentId: String?,

    val bounty: Int,

    val count: PostCounters,

    val voteDir: Int,

    val attachments: List<PostAttachmentResponse>?,

    val createdAt: LocalDateTime
) : Resource {
    override fun getJsonApiDataId(): String {
        return id
    }

    override fun getJsonApiDataType(): String {
        return "posts"
    }

    override fun getRelationships(): Relationships {
        return Relationships.of(Relationship.create("owner", ResourceLinkage.toOne(ownerId, "users")))
    }

    companion object {
        fun of(post: Post): PostResponse {
            return PostResponse(
                id = post.id,
                type = post.type.name,
                approvedCommentId = post.approvedCommentId?.value,
                topic = PostTopicResponse(id = post.topic.topicId, displayName = post.topic.displayName),
                bounty = post.bounty!!.value,
                ownerId = post.owner.userId,
                tags = post.tags.map { it.value },
                title = post.title.value,
                content = post.content.value,
                attachments = post.attachments?.map { PostAttachmentResponse(it.url) },
                count = PostCounters(0, 0),
                voteDir = 1,
                createdAt = post.createdAt
            )
        }
    }
}