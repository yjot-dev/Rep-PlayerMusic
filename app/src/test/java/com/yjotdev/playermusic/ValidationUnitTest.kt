package com.yjotdev.playermusic

import com.yjotdev.playermusic.domain.utils.Validation
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ValidationUnitTest {
    @Test
    fun durationFormatTest() {
        val actual = Validation.durationFormat(350000)
        val expected = "5:50"
        assertEquals(expected, actual)
    }
}