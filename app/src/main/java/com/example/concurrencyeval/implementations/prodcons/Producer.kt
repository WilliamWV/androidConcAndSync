package com.example.concurrencyeval.implementations.prodcons

class Producer(private val millis: Long, private val buffer: GeneralBuffer) : Thread() {
    override fun run() {

        val begin = System.currentTimeMillis()
        var current = begin
        while (current - begin < millis){


            val item = buffer.obtain()

            // do something

            current = System.currentTimeMillis()
        }
    }
}