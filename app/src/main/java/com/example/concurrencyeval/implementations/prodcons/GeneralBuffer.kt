package com.example.concurrencyeval.implementations.prodcons

interface GeneralBuffer {
    val size: Int
    var itensOnBuffer: Int
    var totalProdItems: Int
    var totalConsItems: Int
    fun insert(obj: Any)
    fun obtain(): Any?
}