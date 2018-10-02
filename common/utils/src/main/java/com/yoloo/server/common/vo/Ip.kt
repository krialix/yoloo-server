package com.yoloo.server.common.vo

import com.yoloo.server.common.util.NoArg

@NoArg
data class Ip(var ip: String) : Comparable<Ip> {

    override fun compareTo(other: Ip): Int {
        return ip.compareTo(other.ip)
    }
}
