package com.example.concurrencyeval.implementations.prodcons

import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

class BufferSemaphore(override val size: Int, val millis: Long) : GeneralBuffer {

    override val buffer: Queue<Any?> = ArrayDeque(size)

    override var itensOnBuffer = 0
    override var totalConsItems = 0
    override var totalProdItems = 0

    private val empty = Semaphore(size, true)
    private val full = Semaphore(0, true)
    private val mutex = Semaphore(1, true)


    var begin = System.currentTimeMillis()

    private fun remainingTime(): Long{
        return millis - (System.currentTimeMillis() - begin)
    }


    override fun insert(obj: Any) {
        //if fails to acquire the semaphore within the remaining time returns
        if(!empty.tryAcquire(remainingTime(), TimeUnit.MILLISECONDS)) return
        if(!mutex.tryAcquire(remainingTime(), TimeUnit.MILLISECONDS)) return


        buffer.add(obj)
        itensOnBuffer+=1
        totalProdItems+=1

        mutex.release()
        full.release()

    }

    override fun obtain(): Any? {
        if(!full.tryAcquire(remainingTime(), TimeUnit.MILLISECONDS)) return null
        if(!mutex.tryAcquire(remainingTime(), TimeUnit.MILLISECONDS)) return null

        val item = buffer.poll()
        itensOnBuffer-=1
        totalConsItems+=1

        mutex.release()
        empty.release()

        return item
    }
}