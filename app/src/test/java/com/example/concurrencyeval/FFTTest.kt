package com.example.concurrencyeval

import com.example.concurrencyeval.implementations.prodcons.FFT
import junit.framework.TestCase.assertTrue
import org.junit.Test
import kotlin.math.abs
import kotlin.system.measureTimeMillis

class FFTTest {

    private val tolerance: Double = 0.001

    private fun complexArrayEquals(arr1: Array<FFT.Complex>, arr2: Array<FFT.Complex>): Boolean{
        if (arr1.size != arr2.size) return false
        for (i in arr1.indices){
            if (
                abs(arr1[i].re() - arr2[i].re()) > tolerance ||
                abs(arr1[i].im() - arr2[i].im()) > tolerance
            ) return false
        }
        return true
    }

    @Test
    fun testFFT(){
        val input = FFT.Complex.randComplexArray(1024)
        val transformed = FFT.Complex.fft(input)
        val output = FFT.Complex.ifft(transformed)
        assertTrue(complexArrayEquals(input, output))
    }
}