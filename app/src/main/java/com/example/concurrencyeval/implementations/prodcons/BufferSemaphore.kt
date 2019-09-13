package com.example.concurrencyeval.implementations.prodcons

import java.util.*
import java.util.concurrent.Semaphore

class BufferSemaphore(override val size: Int) : GeneralBuffer {

    override val buffer: Queue<Any?> = ArrayDeque(size)

    override var itensOnBuffer = 0

    private val empty = Semaphore(size, true)
    private val full = Semaphore(0, true)
    private val mutex = Semaphore(1, true)

    override fun insert(obj: Any) {
        empty.acquire()
        mutex.acquire()

        buffer.add(obj)
        itensOnBuffer+=1
        mutex.release()
        full.release()
    }

    override fun obtain(): Any? {
        full.acquire()
        mutex.acquire()

        val item = buffer.poll()
        itensOnBuffer-=1
        mutex.release()
        empty.release()

        return item
    }
}