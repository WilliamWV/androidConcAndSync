package com.example.concurrencyeval.implementations.prodcons

import java.util.*

class BufferSynchronized(override val size: Int) : GeneralBuffer {

    private val buffer: Queue<Any?> = ArrayDeque(size)

    override var totalProdItems: Int = 0
    override var totalConsItems: Int = 0
    override var itensOnBuffer: Int = 0

    override fun insert(obj: Any) {

        try{
            var inserted = false
            while (!inserted){
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
        }catch (e: InterruptedException){
            //do nothing
        }
    }

    override fun obtain(): Any? {
        var item : Any? = null
        try {
            while(item == null) {
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
        } catch (e: InterruptedException) {
            // do nothing
        }
        return item
    }
}