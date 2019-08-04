package com.example.concurrencyeval.implementations.sum

import android.util.Log
import com.example.concurrencyeval.Constants
import kotlin.random.Random

object SumUtil {
    fun randArray(size: Int): LongArray{
        return LongArray(size) { Random.nextLong(0, Constants.ARR_RANGE)}
    }
    fun printArr(arr: LongArray){
        var arrStr = ""
        arr.forEach {
            arrStr+= "$it "
        }
        Log.d("SUM_DEBUG", arrStr)
    }
}