package com.yoloo.server.post.usecase

import com.arcticicestudio.icecore.hashids.Hashids
import com.googlecode.objectify.ObjectifyService.ofy
import com.yoloo.server.common.Exceptions.checkException
import com.yoloo.server.entity.service.EntityCacheService
import com.yoloo.server.like.entity.Like
import com.yoloo.server.post.entity.Bookmark
import com.yoloo.server.post.entity.Post
import com.yoloo.server.post.mapper.PostResponseMapper
import com.yoloo.server.post.util.PostErrors
import com.yoloo.server.post.vo.PostResponse
import com.yoloo.server.usecase.AbstractUseCase
import com.yoloo.server.usecase.UseCase
import com.yoloo.server.user.exception.UserErrors
import org.springframework.stereotype.Service
import org.zalando.problem.Status

@Service
class GetPostByIdUseCase(
    private val hashIds: Hashids,
    private val postResponseMapper: PostResponseMapper,
    private val entityCacheService: EntityCacheService
) : AbstractUseCase<GetPostByIdUseCase.Input, PostResponse>() {

    override fun onExecute(input: Input): PostResponse {
        val postId = hashIds.decode(input.postId)[0]
        val userId = hashIds.decode(input.requesterId)[0]

        val entityCache = entityCacheService.get()

        checkException(entityCache.contains(postId), Status.NOT_FOUND, PostErrors.NOT_FOUND)
        checkException(entityCache.contains(userId), Status.NOT_FOUND, UserErrors.NOT_FOUND)

        val post = ofy().load().key(Post.createKey(postId)).now()

        val self = post.author.isSelf(userId)
        val bookmarked = entityCache.contains(Bookmark.createId(userId, postId))
        val liked = entityCache.contains(Like.createId(userId, postId))

        return postResponseMapper.apply(post, PostResponseMapper.Params(self, liked, bookmarked))
    }

    data class Input(val requesterId: String, val postId: String) : UseCase.Input
}
