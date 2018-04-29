package com.yoloo.server.post.domain.entity

import com.googlecode.objectify.annotation.*
import com.googlecode.objectify.condition.IfNull
import com.yoloo.server.common.shared.Keyable
import com.yoloo.server.common.shared.Validatable
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.domain.vo.Author
import com.yoloo.server.post.domain.vo.PostContent
import com.yoloo.server.post.domain.vo.PostPermission
import com.yoloo.server.post.domain.vo.PostType
import com.yoloo.server.post.domain.vo.postdata.*
import java.time.LocalDateTime
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

    var type: PostType = getPostType(data),

    var permissions: Set<@JvmSuppressWildcards PostPermission> = PostPermission.defaultPermissions(),

    var content: PostContent,

    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Index
    @IgnoreSave(IfNull::class)
    var deletedAt: LocalDateTime? = null
) : Keyable<Post>, Validatable {

    @OnSave
    override fun validate() {
        super.validate()
        /*attachments = attachments ?: emptyList()
        bounty = bounty ?: PostBounty()*/
    }

    fun isDeleted(): Boolean {
        return deletedAt != null
    }

    companion object {
        fun getPostType(data: PostData): PostType {
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