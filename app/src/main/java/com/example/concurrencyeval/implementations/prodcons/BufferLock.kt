package com.example.concurrencyeval.implementations.prodcons

import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

class BufferLock(size: Int, millis: Long) : GeneralBuffer(size, millis){
    private val buffer: Queue<Any?> = ArrayDeque(size)

    private val mutexLock = ReentrantLock(true)
    private val bufferFull = mutexLock.newCondition()
    private val bufferEmpty = mutexLock.newCondition()

    override fun insert(obj: Any) {
        if(!mutexLock.tryLock(remainingTime(), TimeUnit.MILLISECONDS)) {
            return
        }

        while(itensOnBuffer == size){
            val remTime = remainingTime()
            if(!bufferFull.await(remTime, TimeUnit.MILLISECONDS)){
                mutexLock.unlock()
                return
            }

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
            val remTime = remainingTime()


            if(!bufferEmpty.await(remTime, TimeUnit.MILLISECONDS)){
                mutexLock.unlock()

                return null
            }

        }


        val item = buffer.poll()
        itensOnBuffer-=1
        totalConsItems+=1


        bufferFull.signalAll()
        mutexLock.unlock()

        return item
    }
}