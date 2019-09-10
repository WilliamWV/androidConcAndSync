package com.example.concurrencyeval.implementations.mm

import com.example.concurrencyeval.activities.MatMultActivity
import com.example.concurrencyeval.util.RunReport

abstract class MMImplementation (
    val size: Int, val tasks: Int,
    private val activity: MatMultActivity
):Thread(){

    var m1: Array<LongArray> = arrayOf()
    var m2: Array<LongArray> = arrayOf()
    val mAns = Array(size){LongArray(size)}

    fun setMatrixes(m1: Array<LongArray>, m2: Array<LongArray>){
        this.m1 = m1.clone()
        this.m2 = m2.clone()
    }

    fun getAns(): Array<LongArray>{
        return mAns
    }

    init{
        m1 = MMUtil.randMatrix(size)
        m2 = MMUtil.randMatrix(size)
    }

    override fun run() {
        val report = this.execute()
        activity.runOnUiThread {
            activity.updateReport(report)
        }
    }
    abstract fun execute(): RunReport
}