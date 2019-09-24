package com.example.concurrencyeval

import com.example.concurrencyeval.implementations.philosophers.LCS
import junit.framework.TestCase.assertEquals
import org.junit.Test

class LCSTest {


    @Test
    fun testLCS(){

        val s1 = "abcab"
        val s2 = "bdcab"
        assertEquals(4 ,LCS.lcsLength(s1, s2))

        val s3 = "abcdefgh"
        val s4 = "befghijy"
        assertEquals(5, LCS.lcsLength(s3, s4))

    }
}