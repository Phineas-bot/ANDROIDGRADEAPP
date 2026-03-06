package com.example.gradecalculator

import android.app.Application

/**
 * Application class for global app initialization.
 * Can be extended for dependency injection or logging setup.
 */
class GradeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // App-wide initialization can be added here
    }
}
