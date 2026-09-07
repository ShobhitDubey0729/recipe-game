package com.example.rasoifood

import org.junit.Assert.assertEquals
import org.junit.Test

class BuildConfigTest {
    @Test
    fun debugBaseUrlPointsToEmulatorHost() {
        assertEquals("http://10.0.2.2:8000/", BuildConfig.DEBUG_BASE_URL)
    }
}
