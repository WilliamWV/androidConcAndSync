/**
 * This file contains tests to Matrix multiplication problem implemented with the HaMeR framework,
 * these tests are here because they needed some APIs functions to run properly, so these calls
 * could not be mocked on unit tests
 *
 * **/

package com.example.concurrencyeval

import android.support.test.rule.ActivityTestRule
import com.example.concurrencyeval.activities.MatMultActivity
import com.example.concurrencyeval.implementations.mm.MMHaMeR
import com.example.concurrencyeval.util.TestUtil.matrixEqual
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test


class MMHaMeRTests {

    @get:Rule
    var mmActivityRule: ActivityTestRule<MatMultActivity> = ActivityTestRule(MatMultActivity::class.java)

    @Test
    fun mmTest(){
        val tasksToUse = listOf(1, 2, 8, 64)
        val pairTestAns = listOf(
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
        tasksToUse.forEach { tasks ->
            pairTestAns.forEach { triple ->
                val mmThread = MMHaMeR(triple.first.size, tasks, mmActivityRule.activity).also { mm ->
                    mm.setMatrixes(triple.first, triple.second)
                }
                mmThread.start()
                mmThread.join()
                TestCase.assertTrue(matrixEqual(triple.third, mmThread.getAns()))
            }
        }
    }
}