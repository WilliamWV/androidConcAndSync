package com.example.concurrencyeval.implementations.prodcons

import java.util.concurrent.atomic.AtomicReferenceArray

class BufferAtomic(override val size: Int, val millis : Long) : GeneralBuffer{

    private val buffer = AtomicReferenceArray<Any?>(size)
    private val bufferOccupation = AtomicReferenceArray<Boolean>(size)

    override var totalProdItems: Int = 0
    override var totalConsItems: Int = 0
    override var itensOnBuffer: Int = 0

    private val begin = System.currentTimeMillis()

    init {
        for(i in 0 until size){
            bufferOccupation.set(i, false)
        }
    }

    private fun remainingTime(): Long {
        return millis - (System.currentTimeMillis() - begin)
    }

    private fun getAndSetEmptyPosition() : Int{
        for(i in 0 until size){
            if (bufferOccupation.compareAndSet(i, false, true)){
                return i
            }
        }
        return -1
    }

    private fun getAndUnsetOccupiedPosition(): Int {
        for(i in 0 until size){
            if (bufferOccupation.compareAndSet(i, true, false)){
                return i
            }
        }
        return -1
    }

    override fun insert(obj: Any) {
        var position: Int
        var inserted = false
        while(!inserted && remainingTime() > 0) {
            position = getAndSetEmptyPosition()
            if (position != -1) {
                while(!buffer.compareAndSet(position, null, obj)){
                    /***
                     * This while is needed because when 'getAndSetEmptyPosition' is executed the
                     * position may not be really empty, yet, this will happen when this function
                     * executes after 'getAndUnsetOccupiedPosition' but before 'getAndSet' operations
                     * of the function 'obtain' of this file, so this must wait until getAndSet is
                     * executed otherwise the obtained value may not be the expected but the value
                     * that will be inserted by this function
                     *
                     * The function will escape the loop when the value on the position is null,
                     * and so the new object can be safely inserted
                     * * */
                }
                itensOnBuffer += 1
                totalProdItems += 1
                inserted = true
            }
        }
    }

    override fun obtain(): Any? {
        var position: Int
        var obtained = false
        var item: Any? = null
        while(!obtained && remainingTime() > 0){
            position = getAndUnsetOccupiedPosition()
            if(position != -1){
                while (item == null) {
                    item = buffer.getAndSet(position, null)
                    /***
                     * This while is needed for a similar reason of the the while of 'insert'
                     * function, in this case, if this function executes after
                     * 'getAndSetEmptyPosition' but before 'compareAndSet' the item on the obtained
                     * position will still be null
                     * * */
                }
                itensOnBuffer-=1
                totalConsItems+=1
                obtained = true

            }
        }

        return item
    }

}