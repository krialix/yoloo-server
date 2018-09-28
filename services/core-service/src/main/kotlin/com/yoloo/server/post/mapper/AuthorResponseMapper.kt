package com.yoloo.server.post.mapper

import com.arcticicestudio.icecore.hashids.Hashids
import com.yoloo.server.common.vo.Author
import com.yoloo.server.mapper.ResponseMapperWithParams
import com.yoloo.server.mapper.ResponseParams
import com.yoloo.server.post.vo.AuthorResponse
import org.springframework.stereotype.Component

@Component
class AuthorResponseMapper(
    private val hashIds: Hashids
) : ResponseMapperWithParams<Author, AuthorResponse, AuthorResponseMapper.Params> {

    override fun apply(t: Author, u: Params): AuthorResponse {
        return AuthorResponse(
            id = hashIds.encode(t.id),
            self = u.self,
            displayName = t.displayName,
            profileImageUrl = t.profileImageUrl.value
        )
    }

    data class Params(val self: Boolean) : ResponseParams
}
