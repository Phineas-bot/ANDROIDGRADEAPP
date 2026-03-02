package com.example.gradecalculator.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gradecalculator.model.Course

/**
 * Room Database class for persisting course data
 * Provides singleton access to the database
 */
@Database(
    entities = [Course::class],
    version = 1,
    exportSchema = false
)
abstract class GradeDatabase : RoomDatabase() {

    abstract fun courseDao(): CourseDao

    companion object {
        @Volatile
        private var INSTANCE: GradeDatabase? = null

        /**
         * Gets or creates the database instance (singleton pattern)
         * @param context Android context
         * @return GradeDatabase instance
         */
        fun getDatabase(context: Context): GradeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GradeDatabase::class.java,
                    "grade_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

