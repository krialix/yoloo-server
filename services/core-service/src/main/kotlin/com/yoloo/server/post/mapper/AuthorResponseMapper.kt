package com.yoloo.server.post.mapper

import com.arcticicestudio.icecore.hashids.Hashids
import com.yoloo.server.mapper.ResponseMapper
import com.yoloo.server.mapper.ResponseParams
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.vo.*
import org.springframework.stereotype.Component

@Component
class AuthorResponseMapper(
    private val hashIds: Hashids
) : ResponseMapper<Post, AuthorResponse, Unit> {

    override fun apply(t: Post, u: Unit): AuthorResponse {

    }

    data class Params(val self: Boolean) : ResponseParams
}
