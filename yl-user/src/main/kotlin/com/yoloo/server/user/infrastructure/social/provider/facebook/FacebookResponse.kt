package com.yoloo.server.user.infrastructure.social.provider.facebook

import com.yoloo.server.common.util.NoArg

@NoArg
data class FacebookResponse(val id: String, val name: String, val picture: Picture, val email: String) {

    @NoArg
    data class Data(val url: String, val height: Int, val width: Int, val isSilhouette: Boolean)

    @NoArg
    data class Picture(val data: Data)
}