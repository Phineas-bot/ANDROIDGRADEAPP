package com.example.gradecalculator.utils

/**
 * App-wide constants and configuration values.
 */
object Constants {

    // ── Grade Boundaries ───────────────────────────────────────────────────────
    const val GRADE_A_MIN = 80.0
    const val GRADE_B_PLUS_MIN = 70.0
    const val GRADE_B_MIN = 60.0
    const val GRADE_C_PLUS_MIN = 55.0
    const val GRADE_C_MIN = 50.0
    const val GRADE_D_PLUS_MIN = 45.0
    const val GRADE_D_MIN = 40.0
    const val MAX_SCORE = 100.0
    const val MIN_SCORE = 0.0
    const val MAX_CA_SCORE = 40.0
    const val MAX_TEST_SCORE = 60.0

    // ── Excel Column Headers ───────────────────────────────────────────────────
    const val COL_STUDENT_NAME = "Student Name"
    const val COL_CA_SCORE = "CA Score"
    const val COL_TEST_SCORE = "Test Score"
    const val COL_TOTAL_SCORE = "Total Score"
    const val COL_GRADE = "Grade"

    // ── Export File Names ──────────────────────────────────────────────────────
    const val DEFAULT_EXCEL_FILENAME = "student_grades.xlsx"
    const val DEFAULT_PDF_FILENAME = "student_grades_report.pdf"

    // ── UI ──────────────────────────────────────────────────────────────────────
    const val ANIMATION_DURATION_MS = 300
    const val DECIMAL_PLACES = 2
}
