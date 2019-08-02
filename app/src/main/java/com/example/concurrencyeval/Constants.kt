package com.example.concurrencyeval

object Constants {

    // Problems identification

    const val MATRIX_MULT: Int = 0
    const val CONCURR_SUM: Int = 1
    const val PHILOSOPHERS: Int = 2
    const val DOWNLOAD_FILE: Int = 3
    const val UI_BACKGROUND: Int = 4

    // Implementations identification

    const val THREADS: Int = 0
    const val THREAD_POOL: Int = 1
    const val ASYNC_TASK: Int = 2
    const val INTENT_SERV: Int = 3
    const val HAMER: Int = 4
    const val COROUTINES: Int = 5

    // Extras names

    const val PROBLEM_EXTRA = "Problem"
    const val IMPL_EXTRA = "Implementation"

    // Problem names
    val problemNames = hashMapOf(
        MATRIX_MULT to "Matrix multiplication", CONCURR_SUM to "Concurrent sum",
        PHILOSOPHERS to "Philosophers", DOWNLOAD_FILE to "Download file",
        UI_BACKGROUND to "UI background interaction",
        -1 to "No problem selected"
    )

    //Implmentation names
    val implNames = hashMapOf(
        THREADS to "Threads", THREAD_POOL to "Thread pool", ASYNC_TASK to "MMAsyncTask",
        INTENT_SERV to "IntentServices", HAMER to "HaMeR framework", COROUTINES to "Kotlin coroutines",
        -1 to "No implementation selected"
    )

    //Values
    const val MATRIX_RANGE: Long = 15
    const val ARR_RANGE: Long = 15
}