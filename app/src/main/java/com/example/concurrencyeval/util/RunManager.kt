package com.example.concurrencyeval.util

class RunManager {
    var running: Boolean = false
    val waiterObj = Object()

    fun taskStarted(){
        this.running = true
    }

    fun waitUntilComplete(){
        if(this.running) {
            synchronized(waiterObj) {
                waiterObj.wait()
            }
        }
    }

    fun taskCompleted(){
        synchronized(waiterObj) {
            waiterObj.notifyAll()
            this.running = false
        }
    }

}