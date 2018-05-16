package com.yoloo.server.post.response

import com.yoloo.server.common.util.NoArg
import com.yoloo.server.post.response.postdata.PostDataResponse

@NoArg
data class PostResponse(
    val id: String,
    val type: String,
    val author: AuthorResponse,
    val content: String,
    val data: PostDataResponse
)