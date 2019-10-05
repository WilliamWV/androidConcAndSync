package com.example.concurrencyeval.implementations.prodcons

import java.util.*

class BufferSynchronized(size: Int, millis: Long) : GeneralBuffer(size, millis) {

    private val buffer: Queue<Any?> = ArrayDeque(size)

    override fun insert(obj: Any) {

        var inserted = false
        while (!inserted && remainingTime() > 0){
            synchronized(itensOnBuffer){
                if (itensOnBuffer < size) {
                    synchronized(buffer) {
                        buffer.add(obj)
                        itensOnBuffer += 1
                        totalProdItems += 1
                        inserted = true
                    }
                }
            }
        }

    }

    override fun obtain(): Any? {
        var item : Any? = null
        while(item == null && remainingTime() > 0) {
            synchronized(itensOnBuffer) {
                if (itensOnBuffer > 0) {
                    synchronized(buffer) {
                        item = buffer.poll()
                        itensOnBuffer -= 1
                        totalConsItems += 1

                    }
                }
            }
        }
        return item
    }
}