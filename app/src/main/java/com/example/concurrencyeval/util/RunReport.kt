package com.example.concurrencyeval.util

import kotlin.math.pow

class RunReport{
    val time: Long
    val avg: Double
    val std: Double
    val ans: Any?
    val prod: Int
    val cons: Int
    val minExec: Int
    val maxExec: Int

    private val noTime: Long = -1
    private val noAvg: Double = 0.0
    private val noStd: Double = 0.0

    constructor(time: Long){
        this.time = time
        this.avg = this.noAvg
        this.std = this.noStd
        this.ans = null
        this.prod = 0
        this.cons = 0
        this.minExec = 0
        this.maxExec = 0
    }
    constructor(share: IntArray){
        this.time = this.noTime
        this.avg = share.average()

        var diffs = 0.0
        share.forEach {
            diffs += (it - avg).pow(2)
        }
        this.std = diffs/share.size
        this.ans = null
        this.prod = 0
        this.cons = 0
        this.minExec = share.min()!!
        this.maxExec = share.max()!!
    }

    constructor(time: Long, ans: Any?){
        this.time = time
        this.ans = ans
        this.std = this.noStd
        this.avg = this.noAvg
        this.prod = 0
        this.cons = 0
        this.minExec = 0
        this.maxExec = 0
    }

    constructor(producedItems: Int, consumedItems: Int){
        this.prod = producedItems
        this.cons = consumedItems
        this.time = this.noTime
        this.ans = null
        this.std = this.noStd
        this.avg = this.noAvg
        this.minExec = 0
        this.maxExec = 0
    }

}