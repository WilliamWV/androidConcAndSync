package com.example.concurrencyeval.implementations.prodcons

class Consumer(private val millis: Long, private val buffer: GeneralBuffer) : Thread(){
    override fun run() {

        val begin = System.currentTimeMillis()
        var current = begin
        while (current - begin < millis){

            // do something

            val item = 1
            buffer.insert(item)

            current = System.currentTimeMillis()
        }
    }
}