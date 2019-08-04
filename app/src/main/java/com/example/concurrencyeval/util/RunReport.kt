package com.example.concurrencyeval.util

import kotlin.math.pow

class RunReport{
    val time: Long
    val avg: Double
    val std: Double

    private val noTime: Long = -1
    private val noAvg: Double = 0.0
    private val noStd: Double = 0.0

    constructor(time: Long){
        this.time = time
        this.avg = this.noAvg
        this.std = this.noStd
    }
    constructor(share: LongArray){
        this.time = this.noTime
        this.avg = share.average()

        var diffs = 0.0
        share.forEach {
            diffs += (it - avg).pow(2)
        }
        this.std = diffs/share.size
    }

}