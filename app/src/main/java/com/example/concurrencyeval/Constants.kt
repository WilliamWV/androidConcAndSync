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

    // Image names

    const val CAT: Int = 0
    const val DOG: Int = 1
    const val LION: Int = 2
    const val PLATYPUS: Int = 3
    const val PIGEON: Int = 4

    // Extras names

    const val PROBLEM_EXTRA = "Problem"
    const val IMPL_EXTRA = "Implementation"
    const val ACTIVITY_EXTRA = "Activity"
    const val TIME_EXTRA = "Time"
    const val TIME_INTENT = "TimeIntent"
    const val IMG_EXTRA = "Image"

    // Problem names
    val problemNames = hashMapOf(
        MATRIX_MULT to "Matrix multiplication", CONCURR_SUM to "Concurrent sum",
        PHILOSOPHERS to "Philosophers", DOWNLOAD_FILE to "Download file",
        UI_BACKGROUND to "UI background interaction",
        -1 to "No problem selected"
    )

    //Implmentation names
    val implNames = hashMapOf(
        THREADS to "Threads", THREAD_POOL to "Thread pool", ASYNC_TASK to "AsyncTask",
        INTENT_SERV to "IntentServices", HAMER to "HaMeR framework", COROUTINES to "Kotlin coroutines",
        -1 to "No implementation selected"
    )

    //Images URLS
    private const val BASE_URL = "http://inf.ufrgs.br/~wwvargas/test/"
    val imgURL = hashMapOf(
        DOG to BASE_URL + "dog.jpg", CAT to BASE_URL + "cat.jpg", LION to BASE_URL + "lion.jpg",
        PLATYPUS to BASE_URL + "platypus.jpg", PIGEON to BASE_URL + "pigeon.jpg"
    )

    //Values
    const val MATRIX_RANGE: Long = 15
    const val ARR_RANGE: Long = 15
    const val PHILOSOPHERS_FILE: String = "phi_file.txt"
    const val DOWNLOAD_FILE_87_LINK: String = "http://inf.ufrgs.br/~wwvargas/test/87MB.pdf"
    const val DOWNLOAD_FILE_9_LINK: String = "http://inf.ufrgs.br/~wwvargas/test/9MB.pdf"
    const val DOWNLOAD_FILE_DEST: String = "dw/temp_book.pdf"
}