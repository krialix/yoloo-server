package com.yoloo.server.post.api

import com.yoloo.server.post.usecase.WarmUpBookmarkCacheUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RequestMapping(
    "/api/v1/bookmarks",
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
)
@RestController
class BookmarkControllerV1(private val warmUpBookmarkCacheUseCase: WarmUpBookmarkCacheUseCase) {

    @PreAuthorize("hasAuthority('ADMIN') or #oauth2.hasScope('comment:write')")
    @PostMapping("/cache/warmup")
    @ResponseStatus(HttpStatus.CREATED)
    fun warmupCache() {
        warmUpBookmarkCacheUseCase.execute()
    }
}