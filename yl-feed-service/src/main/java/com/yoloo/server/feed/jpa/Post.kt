package com.yoloo.server.feed.jpa

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.common.vo.Media
import com.yoloo.server.feed.converter.MediaCollectionConverter
import com.yoloo.server.feed.converter.PostPermFlagCollectionConverter
import com.yoloo.server.feed.converter.TagCollectionConverter
import com.yoloo.server.feed.vo.PostPermFlag
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@NoArg
@Entity
@Table
class Post(
    @Id
    var id: Long,

    @ManyToOne
    @JoinColumn(name = "author_id")
    var author: Author,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var content: String,

    @ManyToOne
    @JoinColumn(name = "post_group_id")
    var group: Group,

    @Convert(converter = TagCollectionConverter::class)
    @Column(nullable = false)
    var tags: Set<String>,

    @Convert(converter = MediaCollectionConverter::class)
    @Column(nullable = false)
    var medias: List<@JvmSuppressWildcards Media>,

    @Convert(converter = PostPermFlagCollectionConverter::class)
    @Column(nullable = false)
    var flags: Set<@JvmSuppressWildcards PostPermFlag> = EnumSet.noneOf(PostPermFlag::class.java),

    @Column
    var approvedCommentId: Long? = null,

    @Column(nullable = false)
    var bounty: Int = 0,

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column
    var deletedAt: LocalDateTime? = null
)