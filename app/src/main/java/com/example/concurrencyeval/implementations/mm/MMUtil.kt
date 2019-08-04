package com.example.concurrencyeval.implementations.mm

import android.util.Log
import com.example.concurrencyeval.Constants
import kotlin.random.Random

object MMUtil {
    fun randMatrix(size: Int): Array<LongArray>{
        return Array(size){LongArray(size) { Random.nextLong(0, Constants.MATRIX_RANGE)}}
    }
    fun printMatrix(size: Int, mat: Array<LongArray>){
        for (i in 0 until size){

            var line = "| "
            for (j in 0 until size){
                line+=mat[i][j].toString() + " "
            }
            line+= "|"
            Log.d("MM_DEBUG", line)
        }
    }
}