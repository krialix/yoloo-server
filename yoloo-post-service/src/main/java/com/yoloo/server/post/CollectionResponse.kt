package com.yoloo.server.post

data class CollectionResponse<T>(val items: List<T>, val cursor: String?)