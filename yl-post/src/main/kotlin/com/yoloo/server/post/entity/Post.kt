package com.yoloo.server.post.entity

import com.googlecode.objectify.annotation.*
import com.googlecode.objectify.condition.IfNotNull
import com.googlecode.objectify.condition.IfNull
import com.yoloo.server.common.shared.Keyable
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.vo.Author
import com.yoloo.server.post.vo.PostContent
import com.yoloo.server.post.vo.PostPermFlag
import com.yoloo.server.post.vo.PostType
import com.yoloo.server.post.vo.postdata.*
import java.time.LocalDateTime
import java.util.*
import javax.validation.Valid

@NoArg
@Cache
@Entity
data class Post(
    @Id
    var id: Long,

    var author: Author,

    @field:Valid
    var data: PostData,

    var flags: EnumSet<@JvmSuppressWildcards PostPermFlag> = PostPermFlag.default(),

    var content: PostContent,

    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Index(IfNotNull::class)
    @IgnoreSave(IfNull::class)
    var deletedAt: LocalDateTime? = null
) : Keyable<Post> {

    val type: PostType
        get() = toPostType(data)

    fun isDeleted(): Boolean {
        return deletedAt != null
    }

    companion object {
        fun toPostType(data: PostData): PostType {
            return when (data) {
                is TextPostData -> PostType.TEXT
                is RichPostData -> PostType.ATTACHMENT
                is BuddyPostData -> PostType.BUDDY
                is SponsoredPostData -> PostType.SPONSORED
                else -> PostType.TEXT
            }
        }
    }
}