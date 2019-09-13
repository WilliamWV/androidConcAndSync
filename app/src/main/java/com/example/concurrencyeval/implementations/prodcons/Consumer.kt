package com.example.concurrencyeval.implementations.prodcons

import com.example.concurrencyeval.implementations.prodcons.FFT.Complex

@Suppress("UNCHECKED_CAST")
class Consumer(private val millis: Long, private val buffer: GeneralBuffer) : Thread(){
    override fun run() {

        val begin = System.currentTimeMillis()
        var current = begin
        while (current - begin < millis){

            val transformed = buffer.obtain() as Array<Complex>
            val output = Complex.ifft(transformed)

            current = System.currentTimeMillis()
        }
    }
}