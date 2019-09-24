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
import com.example.concurrencyeval.activities.PhilosophersActivity
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FinalPhTest {

    @get:Rule
    var phActivity: ActivityTestRule<PhilosophersActivity> = ActivityTestRule(PhilosophersActivity::class.java, false, false)

    private fun runPhTest(philosophers: Int, time: Int){
        onView(withId(R.id.ph_et_time)).perform(clearText(), typeText(time.toString()), click())
        onView(withId(R.id.ph_et_philosophers)).perform(clearText(), typeText(philosophers.toString()), click())
        onView(isRoot()).perform(closeSoftKeyboard())
        onView(withId(R.id.ph_run_button)).perform(click())
        phActivity.activity.waitTask()
        assertTrue(phActivity.activity.report.avg > 0)
        assertTrue(phActivity.activity.report.std > 0)
    }

    @Test
    fun phTest(){

        val philosophersToUse = listOf(5, 11, 101)
        val timeToTest = listOf(2)
        val implementations = listOf(Constants.THREADS, Constants.THREAD_POOL, Constants.HAMER, Constants.COROUTINES)

        implementations.forEach { impl ->
            val intent = Intent()
            intent.putExtra(Constants.IMPL_EXTRA, impl)
            phActivity.launchActivity(intent)

            timeToTest.forEach{ size ->
                philosophersToUse.forEach { tasks ->
                    runPhTest(tasks, size)
                }
            }
            phActivity.finishActivity()
        }
    }

}