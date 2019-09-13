package com.example.concurrencyeval.implementations.prodcons

import java.util.*

interface GeneralBuffer {
    val buffer : Queue<Any?>
    val size: Int
    var itensOnBuffer: Int
    var totalProdItems: Int
    var totalConsItems: Int
    fun insert(obj: Any)
    fun obtain(): Any?
}