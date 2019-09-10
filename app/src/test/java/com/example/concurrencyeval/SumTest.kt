package com.example.concurrencyeval

import com.example.concurrencyeval.activities.ConcSumActivity
import com.example.concurrencyeval.implementations.sum.SumCoroutines
import com.example.concurrencyeval.implementations.sum.SumThread
import com.example.concurrencyeval.implementations.sum.SumThreadPool
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SumTest {

    @Mock
    private lateinit var activity: ConcSumActivity
    private val pairTestAns = listOf(
        Pair(longArrayOf(2,5,8,12,9,4,3), 43L),
        Pair(longArrayOf(1,13,0,9,18,14,6,2), 63L)
    )
    private val tasksToUse = listOf(1, 2, 8, 512)


    @Test
    fun sumThreadTest(){
        tasksToUse.forEach { tasks ->
            pairTestAns.forEach { pair ->
                val sThread = SumThread(pair.first.size, tasks, activity).also {
                    it.setArr(pair.first)
                }
                sThread.start()
                sThread.join()
                assertEquals(pair.second, sThread.getAns())
            }
        }
    }
    @Test
    fun sumThreadPoolTest(){
        tasksToUse.forEach { tasks ->
            pairTestAns.forEach { pair ->
                val sThreadPool = SumThreadPool(pair.first.size, tasks, activity).also {
                    it.setArr(pair.first)
                }
                sThreadPool.start()
                sThreadPool.join()
                assertEquals(pair.second, sThreadPool.getAns())
            }
        }
    }
    @Test
    fun sumCoroutinesTest(){
        tasksToUse.forEach { tasks ->
            pairTestAns.forEach { pair ->
                val sCoroutines = SumCoroutines(pair.first.size, tasks, activity).also {
                    it.setArr(pair.first)
                }
                sCoroutines.start()
                sCoroutines.join()
                assertEquals(pair.second, sCoroutines.getAns())
            }
        }
    }

}