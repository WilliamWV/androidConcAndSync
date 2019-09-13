/**
 * The code bellow is based on the Java version from:
 * https://www.sanfoundry.com/java-program-compute-discrete-fast-fourier-transform-approach/
 * */

package com.example.concurrencyeval.implementations.prodcons

import kotlin.math.*
import kotlin.random.Random

class FFT {
    class Complex
        (
        private val re: Double
        , private val im: Double
    ) {

        override fun toString(): String {
            if (im == 0.0)
                return re.toString() + ""
            if (re == 0.0)
                return im.toString() + "i"
            return if (im < 0) re.toString() + " - " + -im + "i" else re.toString() + " + " + im + "i"
        }

        fun abs(): Double {
            return hypot(re, im)
        }
        fun phase(): Double {
            return atan2(im, re)
        }
        operator fun plus(b: Complex): Complex {
            val a = this // invoking object
            val real = a.re + b.re
            val imag = a.im + b.im
            return Complex(real, imag)
        }

        // return a new Complex object whose value is (this - b)
        operator fun minus(b: Complex): Complex {
            val a = this
            val real = a.re - b.re
            val imag = a.im - b.im
            return Complex(real, imag)
        }

        operator fun times(b: Complex): Complex {
            val a = this
            val real = a.re * b.re - a.im * b.im
            val imag = a.re * b.im + a.im * b.re
            return Complex(real, imag)
        }

        // scalar multiplication
        operator fun times(alpha: Double): Complex {
            return Complex(alpha * re, alpha * im)
        }

        fun conjugate(): Complex {
            return Complex(re, -im)
        }

        private fun reciprocal(): Complex {
            val scale = re * re + im * im
            return Complex(re / scale, -im / scale)
        }

        fun re(): Double {
            return re
        }

        fun im(): Double {
            return im
        }

        private fun divides(b: Complex): Complex {
            val a = this
            return a.times(b.reciprocal())
        }

        fun exp(): Complex {
            return Complex(exp(re) * cos(im), exp(re) * sin(im))
        }

        private fun sin(): Complex {
            return Complex(sin(re) * cosh(im), cos(re) * sinh(im))
        }

        private fun cos(): Complex {
            return Complex(cos(re) * cosh(im), -sin(re) * sinh(im))
        }

        // return a new Complex object whose value is the complex tangent of
        // this
        fun tan(): Complex {
            return sin().divides(cos())
        }

        companion object {

            // a static version of plus
            fun plus(a: Complex, b: Complex): Complex {
                val real = a.re + b.re
                val imag = a.im + b.im
                return Complex(real, imag)
            }

            // compute the FFT of x[], assuming its length is a power of 2
            fun fft(x: Array<Complex>): Array<Complex> {
                val n = x.size

                // base case
                if (n == 1)
                    return arrayOf(x[0])

                // radix 2 Cooley-Tukey FFT
                if (n % 2 != 0) {
                    throw RuntimeException("N is not a power of 2")
                }

                // fft of even terms
                val even = x.filterIndexed { index, _ -> index%2 == 0 }.toTypedArray()

                val q = fft(even)

                // fft of odd terms
                for (k in 0 until n / 2) {
                    even[k] = x[2 * k + 1]
                }
                val r = fft(even)

                // combine
                val y = arrayOfNulls<Complex>(n)
                for (k in 0 until n / 2) {
                    val kth = -2.0 * k.toDouble() * Math.PI / n
                    val wk = Complex(cos(kth), sin(kth))
                    y[k] = q[k].plus(wk.times(r[k]))
                    y[k + n / 2] = q[k].minus(wk.times(r[k]))
                }
                return y as Array<Complex>
            }

            // compute the inverse FFT of x[], assuming its length is a power of 2
            fun ifft(x: Array<Complex>): Array<Complex> {
                val n = x.size
                // take conjugate
                var y = x.map { item -> item.conjugate() }.toTypedArray()

                // compute forward FFT
                y = fft(y)

                // take conjugate again
                for (i in 0 until n) {
                    y[i] = y[i].conjugate()
                }

                // divide by N
                for (i in 0 until n) {
                    y[i] = y[i].times(1.0 / n)
                }

                return y

            }

            // display an array of Complex numbers to standard output
            fun show(x: Array<Complex>, title: String) {
                println(title)
                for (i in x.indices) {
                    println(x[i])
                }
                println()
            }

            fun randComplexArray(size : Int) : Array<Complex>{
                return Array( size
                ) {
                    Complex(
                        Random.nextInt(-20, 20).toDouble(),
                        Random.nextInt(-20, 20).toDouble()
                    )
                }
            }
        }

    }
}