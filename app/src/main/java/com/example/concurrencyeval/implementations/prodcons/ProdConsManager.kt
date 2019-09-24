package com.example.concurrencyeval.implementations.prodcons

import com.example.concurrencyeval.Constants
import com.example.concurrencyeval.activities.ProdConsActivity
import com.example.concurrencyeval.util.RunReport
import java.util.concurrent.TimeUnit


class ProdConsManager(
    private val bufferType: Int,
    private val producers: Int,
    private val consumers: Int,
    private val bufferSize: Int,
    val activity: ProdConsActivity
) : Thread() {

    private val timeToRun = 1L

    private fun startProcess(buffer: GeneralBuffer){
        val producers = MutableList(producers) { Producer(TimeUnit.SECONDS.toMillis(timeToRun), buffer)}
        val consumers = MutableList(consumers) { Consumer(TimeUnit.SECONDS.toMillis(timeToRun), buffer)}
        producers.forEach { p -> p.start()}
        consumers.forEach { c -> c.start()}
        producers.forEach { p -> p.join()}
        consumers.forEach { c -> c.join()}

        val report = RunReport(buffer.totalProdItems, buffer.totalConsItems)
        activity.runOnUiThread {
            activity.updateReport(report)
        }
    }

    private fun synchronizedProcess(buffer: GeneralBuffer){
        val producers = MutableList(producers) { Producer(TimeUnit.SECONDS.toMillis(timeToRun), buffer)}
        val consumers = MutableList(consumers) { Consumer(TimeUnit.SECONDS.toMillis(timeToRun), buffer)}
        producers.forEach { p -> p.start()}
        consumers.forEach { c -> c.start()}
        sleep(TimeUnit.SECONDS.toMillis(timeToRun))
        producers.forEach { p -> if (p.isAlive) p.interrupt() else p.join()}
        consumers.forEach { c -> if (c.isAlive) c.interrupt() else c.join()}

        val report = RunReport(buffer.totalProdItems, buffer.totalConsItems)
        activity.runOnUiThread {
            activity.updateReport(report)
        }

    }

    override fun run() {

        when(bufferType){
            Constants.SEMAPHORE -> { startProcess(BufferSemaphore(bufferSize, TimeUnit.SECONDS.toMillis(timeToRun)))}
            Constants.LOCK -> {startProcess(BufferLock(bufferSize, TimeUnit.SECONDS.toMillis(timeToRun)))}
            Constants.SYNCHRONIZED -> synchronizedProcess(BufferSynchronized(bufferSize))
            Constants.ATOMIC -> {startProcess(BufferAtomic(bufferSize, TimeUnit.SECONDS.toMillis(timeToRun)))}

            else -> (activity.runOnUiThread{activity.updateReport(RunReport(-1, -1))})
        }
    }
}