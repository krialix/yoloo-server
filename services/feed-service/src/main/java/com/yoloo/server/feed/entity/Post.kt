package com.yoloo.server.feed.entity

import com.googlecode.objectify.Key
import com.googlecode.objectify.annotation.Entity
import com.googlecode.objectify.annotation.Id
import com.googlecode.objectify.annotation.IgnoreSave
import com.googlecode.objectify.condition.IfNull
import com.yoloo.server.common.entity.BaseEntity
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.Author
import com.yoloo.server.common.vo.Media
import com.yoloo.server.feed.vo.PostGroup
import com.yoloo.server.feed.vo.PostPermFlag
import com.yoloo.server.feed.vo.PostTitle
import java.util.*

@NoArg
@Entity
data class Post(
    @Id
    var id: Long,

    var author: Author,

    var title: PostTitle,

    var content: String,

    var group: PostGroup,

    var tags: Set<String>,

    var medias: List<@JvmSuppressWildcards Media>,

    var flags: Set<@JvmSuppressWildcards PostPermFlag> = EnumSet.noneOf(PostPermFlag::class.java),

    @IgnoreSave(IfNull::class)
    var approvedCommentId: Long? = null,

    var bounty: Int = 0
) : BaseEntity<Post>() {

    override fun onLoad() {
        super.onLoad()
        @Suppress("USELESS_ELVIS")
        flags = flags ?: emptySet()
    }

    companion object {
        fun createKey(postId: Long): Key<Post> {
            return Key.create(Post::class.java, postId)
        }
    }
}