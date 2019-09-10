package com.example.concurrencyeval.implementations.sum

import com.example.concurrencyeval.activities.ConcSumActivity
import com.example.concurrencyeval.util.RunReport

abstract class SumImplementation (
    var numbers: Int, var tasks: Int,
    var activity: ConcSumActivity
): Thread(){

    var mArr : LongArray = longArrayOf()

    init {
        mArr = SumUtil.randArray(numbers)
    }

    fun setArr(arr: LongArray){
        mArr = arr.clone()
    }

    //may be called only after the execution of the sum is complete
    fun getAns() : Long{
        return mArr[0]
    }

    override fun run() {
        val report = this.execute()
        activity.runOnUiThread {
            activity.updateReport(report)
        }
    }
    abstract fun execute(): RunReport
}