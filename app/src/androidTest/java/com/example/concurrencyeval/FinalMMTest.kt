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
import android.util.Log
import com.example.concurrencyeval.activities.MatMultActivity
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FinalMMTest {

    @get:Rule
    var mmActivity: ActivityTestRule<MatMultActivity> = ActivityTestRule(MatMultActivity::class.java, false, false)

    private fun runMMTest(tasks: Int, size: Int){
        onView(withId(R.id.mm_et_size)).perform(clearText(), typeText(size.toString()), click())
        onView(withId(R.id.mm_et_tasks)).perform(clearText(), typeText(tasks.toString()), click())
        onView(isRoot()).perform(closeSoftKeyboard())
        onView(withId(R.id.mm_run_button)).perform(click())
        mmActivity.activity.waitTask()
        Log.d("TEST_ESPRESSO", mmActivity.activity.report.time.toString())
        //assertTrue(mmActivity.activity.report.time > 0)
    }

    @Test
    fun mmTest(){

        val tasksToUse = listOf(1, 2, 8, 64)
        val sizesToTest = listOf(128, 256, 512)
        val implementations = listOf(Constants.THREADS, Constants.THREAD_POOL, Constants.HAMER, Constants.COROUTINES)

        implementations.forEach { impl ->
            val intent = Intent()
            intent.putExtra(Constants.IMPL_EXTRA, impl)
            mmActivity.launchActivity(intent)

            sizesToTest.forEach{ size ->
                tasksToUse.forEach { tasks ->
                    runMMTest(tasks, size)
                }
            }
            mmActivity.finishActivity()
        }
    }

}