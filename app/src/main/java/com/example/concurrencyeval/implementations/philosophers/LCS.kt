package com.example.concurrencyeval.implementations.philosophers

import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random

class LCS {

    class LCSMatrix (private val s1: String, private val s2: String){


        private val matrix: Array<Array<Int>> = Array(s1.length + 1){Array(s2.length + 1){0}}
        private var computed = false

        private fun computeMatrix(){
            for(i in 1 until s1.length + 1){
                for(j in 1 until s2.length + 1){
                    matrix[i][j] =
                        if (s1[i-1] == s2[j-1])
                            matrix[i-1][j-1] + 1
                        else max(matrix[i-1][j], matrix[i][j-1])
                }
            }
        }

        fun getLCSLength() : Int{
            if (!this.computed)
                this.computeMatrix()
            return matrix.last().last()
        }


    }

    companion object {
        fun lcsLength(s1: String, s2: String): Int{
            return LCSMatrix(s1, s2).getLCSLength()
        }

        //range is the number of characters to consider to generate the maximum string
        fun randString(range: Int, length: Int): String{
            assert(range < 26)
            return Array(length){ abs(Random.nextInt()) % range }.map { ('a'.toInt() + it).toChar() }.toString()
        }

    }
}