package com.example.concurrencyeval.implementations.philosophers

import android.util.Log


object PhUtil {


    fun printAll(arr: IntArray){
        Log.d("PH_DEBUG", "Number of times each philosopher have eaten:")
        arr.forEach {
           Log.d("PH_DEBUG", it.toString())
        }
    }
}