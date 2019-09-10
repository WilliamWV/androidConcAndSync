package com.example.concurrencyeval

import com.example.concurrencyeval.activities.MatMultActivity
import com.example.concurrencyeval.implementations.mm.MMCoroutines
import com.example.concurrencyeval.implementations.mm.MMThread
import com.example.concurrencyeval.implementations.mm.MMThreadPool
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MMTest {
    @Mock
    private lateinit var activity: MatMultActivity
    private val pairTestAns = listOf(
        Triple(arrayOf(
            longArrayOf(2, 4, 5),
            longArrayOf(3, -1, 7),
            longArrayOf(1, 0, 8)
        ), arrayOf(
            longArrayOf(1, 3, 4),
            longArrayOf(2, 6, 10),
            longArrayOf(6, 9, 5)
        ), arrayOf(
            longArrayOf(40, 75, 73),
            longArrayOf(43, 66, 37),
            longArrayOf(49, 75, 44)
        )),
        Triple(arrayOf(
            longArrayOf(1, 3),
            longArrayOf(4, 5)
        ), arrayOf(
            longArrayOf(5, 7),
            longArrayOf(9, 0)
        ), arrayOf(
            longArrayOf(32, 7),
            longArrayOf(65, 28)
        ))
    )
    private val tasksToUse = listOf(1, 2, 8, 64)

    private fun matrixEqual(m1: Array<LongArray>, m2: Array<LongArray>) : Boolean {
        if (m1.size != m2.size) return false
        for (i in m1.indices){
            if (m1[i].size != m2[i].size) return false
            for (j in m1[i].indices){
                if (m1[i][j] != m2[i][j]) return false
            }
        }
        return true
    }

    @Test
    fun mmThreadTest(){
        tasksToUse.forEach { tasks ->
            pairTestAns.forEach { triple ->
                val mmThread = MMThread(triple.first.size, tasks, activity).also { mm ->
                    mm.setMatrixes(triple.first, triple.second)
                }
                mmThread.start()
                mmThread.join()
                TestCase.assertTrue(matrixEqual(triple.third, mmThread.getAns()))
            }
        }
    }
    @Test
    fun mmThreadPoolTest(){
        tasksToUse.forEach { tasks ->
            pairTestAns.forEach { triple ->
                val mmThreadPool = MMThreadPool(triple.first.size, tasks, activity).also { mm ->
                    mm.setMatrixes(triple.first, triple.second)
                }
                mmThreadPool.start()
                mmThreadPool.join()
                TestCase.assertTrue(matrixEqual(triple.third, mmThreadPool.getAns()))
            }
        }
    }
    @Test
    fun mmCoroutinesTest(){
        tasksToUse.forEach { tasks ->
            pairTestAns.forEach { triple ->
                val mmCoroutines = MMCoroutines(triple.first.size, tasks, activity).also { mm ->
                    mm.setMatrixes(triple.first, triple.second)
                }
                mmCoroutines.start()
                mmCoroutines.join()
                TestCase.assertTrue(matrixEqual(triple.third, mmCoroutines.getAns()))
            }
        }
    }


}