package com.power.tesseract.services

import android.app.job.JobParameters
import android.app.job.JobService

class MyJobService : JobService() {
    override fun onStartJob(p0: JobParameters?): Boolean {
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return true
    }
}