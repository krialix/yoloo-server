package com.yoloo.server.feed.jpa

import com.yoloo.server.common.util.NoArg
import javax.persistence.*

@NoArg
@Entity
@Table
class Author(
    @Id
    var id: Long,

    @Column(nullable = false)
    var displayName: String,

    @Column(nullable = false)
    var profileImageUrl: String,

    @OneToMany(mappedBy = "author")
    var posts: Set<Post>
)