package com.yoloo.server.user.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class Screen(var dpi: Dpi, var height: Int, var width: Int) {

    @NoArg
    data class Dpi(var value: Int)
}