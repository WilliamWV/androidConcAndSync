package com.example.concurrencyeval.util

object TestUtil {
    fun matrixEqual(m1: Array<LongArray>, m2: Array<LongArray>) : Boolean {
        if (m1.size != m2.size) return false
        for (i in m1.indices){
            if (m1[i].size != m2[i].size) return false
            for (j in m1[i].indices){
                if (m1[i][j] != m2[i][j]) return false
            }
        }
        return true
    }
}