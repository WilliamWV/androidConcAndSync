package com.example.concurrencyeval.implementations.philosophers

import com.example.concurrencyeval.Constants
import java.io.File


class PhWorkerRunnable(
    var fork1: String, var fork2: String, val time: Int, val file: File, val id: Int
): Runnable{


    override fun run() {
        val beg = System.currentTimeMillis()
        var end = beg
        while ((end - beg) / 1000 <= time){
            synchronized(fork1){
                synchronized(fork2){
                    LCS.lcsLength(fork1, fork2)
                    fork1 = LCS.randString(Constants.LCS_RANGE, Constants.LCS_LENGTH)
                    fork2 = LCS.randString(Constants.LCS_RANGE, Constants.LCS_LENGTH)

                    file.appendText("$id\n")
                }
            }
            end = System.currentTimeMillis()
        }
    }
}