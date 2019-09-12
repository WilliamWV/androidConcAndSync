package com.example.concurrencyeval

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText

object EspressoUtil {
    fun clickOnViewId(id:Int ){
        onView(withId(id)).perform(click())
    }
    fun clickOnViewText(text: String){
        onView(withText(text)).perform(click())
    }
}