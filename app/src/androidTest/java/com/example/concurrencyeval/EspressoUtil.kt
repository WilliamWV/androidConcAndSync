package com.example.concurrencyeval

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText


object EspressoUtil {
    fun clickOnViewId(id:Int ){
        onView(withId(id)).perform(click())
    }
    fun clickOnViewText(text: String){
        onView(withText(text)).perform(click())
    }
}