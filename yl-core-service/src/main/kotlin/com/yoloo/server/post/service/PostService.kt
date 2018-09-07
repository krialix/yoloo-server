package com.yoloo.server.post.service

import com.yoloo.server.post.vo.CreatePostRequest
import com.yoloo.server.post.vo.PostResponse
import com.yoloo.server.post.vo.UpdatePostRequest

interface PostService {

    fun getPost(userId: Long, postId: Long): PostResponse

    fun createPost(userId: Long, request: CreatePostRequest): PostResponse

    fun updatePost(userId: Long, postId: Long, request: UpdatePostRequest)

    fun deletePost(userId: Long, postId: Long)
}
