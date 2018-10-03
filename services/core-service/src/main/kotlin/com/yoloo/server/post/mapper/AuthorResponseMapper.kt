package com.yoloo.server.post.mapper

import com.arcticicestudio.icecore.hashids.Hashids
import com.yoloo.server.common.vo.Author
import com.yoloo.server.mapper.ResponseMapper
import com.yoloo.server.post.vo.AuthorResponse
import org.springframework.stereotype.Component

@Component
class AuthorResponseMapper(private val hashIds: Hashids) : ResponseMapper<Author, AuthorResponse> {

    override fun apply(t: Author): AuthorResponse {
        return AuthorResponse(
            id = hashIds.encode(t.id),
            self = t.self,
            displayName = t.displayName,
            profileImageUrl = t.profileImageUrl.value
        )
    }
}
