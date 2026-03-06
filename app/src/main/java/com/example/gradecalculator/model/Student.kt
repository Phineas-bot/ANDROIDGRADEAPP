package com.example.gradecalculator.model

import java.util.UUID

/**
 * Data class representing a Student with their scores and calculated grade.
 * Demonstrates OOP data class design with computed properties.
 *
 * @property id Unique identifier for this student record
 * @property studentName Name of the student
 * @property caScore Continuous Assessment score
 * @property testScore Test score
 * @property totalScore Total score (CA + Test), auto-computed if not provided
 * @property grade Letter grade assigned after calculation
 */
data class Student(
    val id: String = UUID.randomUUID().toString(),
    val studentName: String,
    val caScore: Double,
    val testScore: Double,
    val totalScore: Double = caScore + testScore,
    val grade: String = ""
) {
    /**
     * Computed property using lambda-style getter:
     * Checks if the student has a passing grade (D or above).
     */
    val isPassing: Boolean
        get() = grade != "F" && grade.isNotEmpty()

    /**
     * Returns a formatted single-line summary of this student.
     * Uses string template (Kotlin idiom).
     */
    fun toSummary(): String =
        "$studentName | CA: $caScore | Test: $testScore | Total: $totalScore | Grade: $grade"

    override fun toString(): String =
        "Student(name=$studentName, CA=$caScore, Test=$testScore, Total=$totalScore, Grade=$grade)"
}
