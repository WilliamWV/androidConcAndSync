package com.example.concurrencyeval

import com.example.concurrencyeval.implementations.prodcons.*
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit


class PCTest {


    private fun testBuffer(buffer: GeneralBuffer){
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

    @Test
    fun testBufferSemaphore() {
        testBuffer(BufferSemaphore(4, TimeUnit.SECONDS.toMillis(1)))
    }
    @Test
    fun testBufferLock() {
        testBuffer(BufferLock(4, TimeUnit.SECONDS.toMillis(1)))
    }
    @Test
    fun testBufferSynchronized() {
        testBuffer(BufferSynchronized(4, TimeUnit.SECONDS.toMillis(1)))
    }
    @Test
    fun testBufferAtomic() {
        testBuffer(BufferAtomic(4, TimeUnit.SECONDS.toMillis(1)))
    }


}