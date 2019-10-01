/**
 * This file contains tests to concurrent  problem implemented with the HaMeR framework,
 * these tests are here because they needed some APIs functions to run properly, so these calls
 * could not be mocked on unit tests
 *
 * **/

package com.example.concurrencyeval

import androidx.test.rule.ActivityTestRule
import com.example.concurrencyeval.activities.ConcSumActivity
import com.example.concurrencyeval.implementations.sum.SumHaMeR
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test

class SumHaMeRTest {

    @get:Rule
    var sumActivityRule: ActivityTestRule<ConcSumActivity> = ActivityTestRule(ConcSumActivity::class.java)

    @Test
    fun sumCoroutinesTest(){
        val pairTestAns = listOf(
            Pair(longArrayOf(2,5,8,12,9,4,3), 43L),
            Pair(longArrayOf(1,13,0,9,18,14,6,2), 63L)
        )
        val tasksToUse = listOf(1, 2, 8, 512)

        tasksToUse.forEach { tasks ->
            pairTestAns.forEach { pair ->
                val sCoroutines = SumHaMeR(pair.first.size, tasks, sumActivityRule.activity).also {
                    it.setArr(pair.first)
                }
                sCoroutines.start()
                sCoroutines.join()
                TestCase.assertEquals(pair.second, sCoroutines.getAns())
            }
        }
    }
}