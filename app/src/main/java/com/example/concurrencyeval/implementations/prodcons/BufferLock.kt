package com.example.concurrencyeval.implementations.prodcons

import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

class BufferLock(override val size: Int, private val millis: Long) : GeneralBuffer{
    override val buffer: Queue<Any?> = ArrayDeque(size)

    override var itensOnBuffer = 0
    override var totalConsItems = 0
    override var totalProdItems = 0

    private val mutexLock = ReentrantLock(true)
    private val bufferFull = mutexLock.newCondition()
    private val bufferEmpty = mutexLock.newCondition()

    private var begin = System.currentTimeMillis()

    private fun remainingTime(): Long{
        return millis - (System.currentTimeMillis() - begin)
    }

    override fun insert(obj: Any) {
        if(!mutexLock.tryLock(remainingTime(), TimeUnit.MILLISECONDS)) {
            return
        }
        while(itensOnBuffer == size){
            if(!bufferFull.await(remainingTime(), TimeUnit.MILLISECONDS)) return

        }

        buffer.add(obj)
        itensOnBuffer+=1
        totalProdItems+=1
        bufferEmpty.signalAll()
        mutexLock.unlock()

    }

    override fun obtain(): Any? {

        if(!mutexLock.tryLock(remainingTime(), TimeUnit.MILLISECONDS)){
            return null
        }
        while (itensOnBuffer == 0){
            if(!bufferEmpty.await(remainingTime(), TimeUnit.MILLISECONDS)) return null
        }
        val item = buffer.poll()
        itensOnBuffer-=1
        totalConsItems+=1

        bufferFull.signalAll()
        mutexLock.unlock()
        return item
    }
}