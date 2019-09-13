package com.example.concurrencyeval.implementations.prodcons

import android.util.Log

class Producer(private val millis: Long, private val buffer: GeneralBuffer) : Thread() {
    override fun run() {

        val begin = System.currentTimeMillis()
        var current = begin
        while (current - begin < millis){

            val arrSize = 1024
            val input = FFT.Complex.randComplexArray(arrSize)
            val transform = FFT.Complex.fft(input)

            buffer.insert(transform)

            current = System.currentTimeMillis()
        }
    }
}