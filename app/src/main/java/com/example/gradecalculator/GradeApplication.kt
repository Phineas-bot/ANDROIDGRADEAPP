package com.example.gradecalculator

import android.app.Application
import com.example.gradecalculator.data.local.GradeDatabase

/**
 * Application class for global app initialization
 * Can be used for setting up dependencies and logging
 */
class GradeApplication : Application() {

    companion object {
        lateinit var database: GradeDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize database
        database = GradeDatabase.getDatabase(this)
    }
}

