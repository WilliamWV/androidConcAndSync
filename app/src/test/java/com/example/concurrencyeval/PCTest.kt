package com.example.concurrencyeval

import com.example.concurrencyeval.implementations.prodcons.BufferSemaphore
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit


class PCTest {


    @Test
    fun testBufferSemaphore() {
        val buffer = BufferSemaphore(4, TimeUnit.SECONDS.toMillis(1))
        assertEquals(0, buffer.itensOnBuffer)
        buffer.insert("String item")
        assertEquals(1, buffer.itensOnBuffer)
        buffer.insert(1)
        buffer.insert(1.5)
        buffer.insert('W')
        assertEquals(4, buffer.itensOnBuffer)
        assertEquals("String item", buffer.obtain().toString())
        buffer.obtain()
        assertEquals(2, buffer.itensOnBuffer)
    }

}