package com.example.concurrencyeval.implementations.prodcons

import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

class BufferSemaphore(size: Int, millis: Long) : GeneralBuffer(size, millis){

    private val buffer: Queue<Any?> = ArrayDeque(size)

    private val empty = Semaphore(size, true)
    private val full = Semaphore(0, true)
    private val mutex = Semaphore(1, true)


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