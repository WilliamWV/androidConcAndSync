package com.example.concurrencyeval

object Constants {

    // Problems identification

    const val MATRIX_MULT: Int = 0
    const val CONCURR_SUM: Int = 1
    const val PHILOSOPHERS: Int = 2
    const val DOWNLOAD_FILE: Int = 3

    // Implementations identification

    const val THREADS: Int = 0
    const val THREAD_POOL: Int = 1
    const val ASYNC_TASK: Int = 2
    const val INTENT_SERV: Int = 3
    const val HAMER: Int = 4
    const val COROUTINES: Int = 5

    // Image names

    private const val CAT: Int = 0
    private const val DOG: Int = 1
    private const val LION: Int = 2
    private const val PLATYPUS: Int = 3
    private const val PIGEON: Int = 4

    // Extras names

    const val PROBLEM_EXTRA = "Problem"
    const val IMPL_EXTRA = "Implementation"
    const val TIME_EXTRA = "Time"
    const val TIME_INTENT = "TimeIntent"
    const val IMG_EXTRA = "Image"

    // Problem names
    val problemNames = hashMapOf(
        MATRIX_MULT to "Matrix multiplication", CONCURR_SUM to "Concurrent sum",
        PHILOSOPHERS to "Philosophers", DOWNLOAD_FILE to "Download file",
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

    //General UI items

    const val RUN_BUTTON = 0
    const val DESCRIPTION_TV = 1
    const val PROGRESS_BAR = 2

    val problemsUIIds = hashMapOf(
        MATRIX_MULT to hashMapOf(
            RUN_BUTTON to R.id.mm_run_button,
            DESCRIPTION_TV to R.id.mm_tv_description,
            PROGRESS_BAR to R.id.mm_progressBar
        ),
        CONCURR_SUM to hashMapOf(
            RUN_BUTTON to R.id.cs_run_button,
            DESCRIPTION_TV to R.id.cs_tv_description,
            PROGRESS_BAR to R.id.cs_progressBar
        ),
        PHILOSOPHERS to hashMapOf(
            RUN_BUTTON to R.id.ph_run_button,
            DESCRIPTION_TV to R.id.ph_tv_description,
            PROGRESS_BAR to R.id.ph_progressBar
        ),
        DOWNLOAD_FILE to hashMapOf(
            RUN_BUTTON to R.id.fd_run_button,
            DESCRIPTION_TV to R.id.fd_tv_description,
            PROGRESS_BAR to R.id.fd_progressBar
        )
    )

    //Values
    const val MATRIX_RANGE: Long = 15
    const val ARR_RANGE: Long = 15
    const val PHILOSOPHERS_FILE: String = "phi_file.txt"
}