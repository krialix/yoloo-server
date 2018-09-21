package com.yoloo.server.feed.api

import com.yoloo.server.common.vo.response.CollectionResponse
import com.yoloo.server.feed.service.FeedService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/feeds")
class FeedController(private val feedService: FeedService) {

    @GetMapping("/anonymous")
    fun listAnonymousFeed(@RequestParam("cursor", required = false) cursor: String?): CollectionResponse {
        return feedService.listAnonymousFeed(cursor)
    }
}