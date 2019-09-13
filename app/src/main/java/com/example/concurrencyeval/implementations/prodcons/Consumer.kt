package com.example.concurrencyeval.implementations.prodcons

import android.util.Log
import com.example.concurrencyeval.implementations.prodcons.FFT.Complex

@Suppress("UNCHECKED_CAST")
class Consumer(private val millis: Long, private val buffer: GeneralBuffer) : Thread(){
    override fun run() {

        val begin = System.currentTimeMillis()
        var current = begin
        while (current - begin < millis){

            val item = buffer.obtain()
            if (item != null) {
                val transformed = item as Array<Complex>
                Complex.ifft(transformed)
            }
            current = System.currentTimeMillis()
        }
    }
}