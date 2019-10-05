package com.example.concurrencyeval.implementations.prodcons

abstract class GeneralBuffer (val size: Int, private val millis: Long){
    var itensOnBuffer: Int = 0
    var totalProdItems: Int = 0
    var totalConsItems: Int = 0
    abstract fun insert(obj: Any)
    abstract fun obtain(): Any?

    private val begin = System.currentTimeMillis()
    fun remainingTime(): Long{
        return millis - (System.currentTimeMillis() - begin)
    }
}