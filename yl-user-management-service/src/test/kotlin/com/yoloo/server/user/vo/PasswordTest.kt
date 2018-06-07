package com.yoloo.server.user.vo

import org.junit.Assert.assertEquals
import org.junit.Test

class PasswordTest {

    @Test
    fun testPasswordTrim() {
        val password = Password("hello ")

        assertEquals(password.value, "hello")
    }
}