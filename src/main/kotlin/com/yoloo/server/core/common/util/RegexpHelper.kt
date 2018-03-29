package com.yoloo.server.core.common.util

object RegexpHelper {

    private const val zeroTo255 = "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])"

    const val IP_REGEXP = "$zeroTo255\\.$zeroTo255\\.$zeroTo255\\.$zeroTo255"
}