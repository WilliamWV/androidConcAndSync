package com.example.concurrencyeval.implementations.philosophers

import java.io.File


class PhWorkerRunnable(
    val fork1: Any, val fork2: Any, val time: Int, val file: File, val id: Int
): Runnable{


    override fun run() {
        val beg = System.currentTimeMillis()
        var end = beg
        while ((end - beg) / 1000 <= time){
            synchronized(fork1){
                synchronized(fork2){
                    file.appendText("$id\n")
                }
            }
            end = System.currentTimeMillis()
        }
    }
}