/**
 * This file contains tests to Matrix multiplication problem implemented with the HaMeR framework,
 * these tests are here because they needed some APIs functions to run properly, so these calls
 * could not be mocked on unit tests
 *
 * **/

package com.example.concurrencyeval

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.isRoot
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.example.concurrencyeval.activities.ConcSumActivity
import com.example.concurrencyeval.util.RunReport
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException


@RunWith(AndroidJUnit4::class)
class FinalSumTest : GeneralInstrTest{
    override fun analizeReports() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override var reports: MutableList<RunReport> = mutableListOf()


    @get:Rule
    var csActivity: ActivityTestRule<ConcSumActivity> = ActivityTestRule(ConcSumActivity::class.java, false, false)

    private fun performInteractions(tasks: Int, numbers: Int){
        onView(withId(R.id.cs_et_numbers)).perform(clearText(), typeText(numbers.toString()), click())
        onView(withId(R.id.cs_et_tasks)).perform(clearText(), typeText(tasks.toString()), click())
        onView(isRoot()).perform(closeSoftKeyboard())
        onView(withId(R.id.cs_run_button)).perform(click())
        csActivity.activity.waitTask()
        assertTrue(csActivity.activity.report.time > 0)
        reports.add(csActivity.activity.report)
    }

    private fun runSumTest(tasks: Int, numbers: Int, fails: Int = 0){
        try {
            performInteractions(tasks, numbers)
        }catch(te: TimeoutException){
            // try again if number of fails does not have passed maximum
            if (fails >= Constants.MAX_TIMEOUT_FAILS) throw te
            else runSumTest(tasks, numbers, fails + 1)
        }
    }

    @Test
    override fun runTest(){

        val tasksToUse = listOf(1, 2, 8, 32, 256)
        val numbersToTest = listOf(1048576)
        val implementations = listOf(Constants.THREADS, Constants.THREAD_POOL, Constants.HAMER, Constants.COROUTINES, Constants.THREADS_BARRIER)

        implementations.forEach { impl ->
            val intent = Intent()
            intent.putExtra(Constants.IMPL_EXTRA, impl)
            csActivity.launchActivity(intent)

            numbersToTest.forEach{ numbers ->
                tasksToUse.forEach { tasks ->
                    runSumTest(tasks, numbers)
                }
            }
            csActivity.finishActivity()
        }
    }

}