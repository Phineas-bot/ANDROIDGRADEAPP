package com.example.gradecalculator.utils

/**
 * App-wide constants and configuration values
 */
object Constants {

    // Database
    const val DATABASE_NAME = "grade_database"

    // Scores limits
    const val MAX_CA_SCORE = 40.0
    const val MAX_EXAM_SCORE = 60.0
    const val MAX_TOTAL_SCORE = 100.0

    // Grade boundaries
    const val GRADE_A_MIN = 70.0
    const val GRADE_B_MIN = 60.0
    const val GRADE_C_MIN = 50.0
    const val GRADE_D_MIN = 45.0

    // Grade points
    const val GRADE_POINT_A = 4.0
    const val GRADE_POINT_B = 3.0
    const val GRADE_POINT_C = 2.0
    const val GRADE_POINT_D = 1.0
    const val GRADE_POINT_F = 0.0

    // GPA threshold
    const val GPA_PASS_THRESHOLD = 2.0

    // UI
    const val DEBOUNCE_DELAY_MS = 300L
    const val ANIMATION_DURATION_MS = 300

    // Decimal places for rounding
    const val DECIMAL_PLACES = 2
}

