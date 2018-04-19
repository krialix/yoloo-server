package com.yoloo.server.post.domain.vo.postdata

import com.googlecode.objectify.annotation.Subclass
import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.domain.vo.PostAttachment
import com.yoloo.server.post.domain.vo.PostTitle
import javax.validation.Valid

@Subclass
@NoArg
data class SponsoredPostData(
    @field:Valid
    var title: PostTitle,

    var attachments: List<@JvmSuppressWildcards PostAttachment>? = null
) : PostData()