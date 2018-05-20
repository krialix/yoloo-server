package com.yoloo.server.post.vo

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.vo.postdataresponse.PostDataResponse

@NoArg
data class PostResponse(
    val id: Long,
    val type: String,
    val author: AuthorResponse,
    val content: String,
    val data: PostDataResponse
)