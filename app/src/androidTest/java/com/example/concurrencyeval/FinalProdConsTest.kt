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
import com.example.concurrencyeval.activities.ProdConsActivity
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FinalProdConsTest {

    @get:Rule
    var pcActivity: ActivityTestRule<ProdConsActivity> = ActivityTestRule(ProdConsActivity::class.java, false, false)

    private fun runPcTest(producers: Int, consumers: Int, buffSize: Int){
        onView(withId(R.id.pc_et_producers)).perform(clearText(), typeText(producers.toString()), click())
        onView(withId(R.id.pc_et_consumers)).perform(clearText(), typeText(consumers.toString()), click())
        onView(withId(R.id.pc_et_buffer)).perform(clearText(), typeText(buffSize.toString()), click())
        onView(isRoot()).perform(closeSoftKeyboard())
        onView(withId(R.id.pc_run_button)).perform(click())
        pcActivity.activity.waitTask()
        assertTrue(pcActivity.activity.report.prod > 0)
        assertTrue(pcActivity.activity.report.cons > 0)
    }

    @Test
    fun pcTest(){

        val producersToTest = listOf(5, 10, 20)
        val consumersToTest = listOf(5, 10, 20)
        val bufferToUse = listOf(2, 5, 10)

        val implementations = listOf(Constants.SEMAPHORE, Constants.SYNCHRONIZED, Constants.LOCK, Constants.ATOMIC)

        implementations.forEach { impl ->
            val intent = Intent()
            intent.putExtra(Constants.IMPL_EXTRA, impl)
            pcActivity.launchActivity(intent)

            bufferToUse.forEach{ buffSize ->
                producersToTest.forEach { producers ->
                    consumersToTest.forEach { consumers ->
                        runPcTest(producers, consumers, buffSize)
                    }
                }
            }
            pcActivity.finishActivity()
        }
    }

}