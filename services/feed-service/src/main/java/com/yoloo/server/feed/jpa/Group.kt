package com.yoloo.server.feed.jpa

import com.yoloo.server.common.util.NoArg
import javax.persistence.*

@NoArg
@Entity
@Table(name = "post_group")
class Group(
    @Id
    @Column(name = "group_id")
    var id: Long,

    @Column(nullable = false)
    var displayName: String,

    @OneToMany(mappedBy = "group", cascade = [CascadeType.ALL], orphanRemoval = true)
    var posts: Set<Post>
)
