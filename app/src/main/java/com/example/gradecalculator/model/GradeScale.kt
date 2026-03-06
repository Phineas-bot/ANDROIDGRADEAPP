package com.example.gradecalculator.model

/**
 * Enum class representing the grading scale with score boundaries.
 * Demonstrates OOP enum design with properties and companion object methods.
 *
 * Each entry defines a grade range based on the Total Score.
 *
 * | Grade | Min Score | Max Score |
 * |-------|-----------|-----------|
 * | A     | 80.0      | 100.0     |
 * | B+    | 70.0      | 79.99     |
 * | B     | 60.0      | 69.99     |
 * | C+    | 55.0      | 59.99     |
 * | C     | 50.0      | 54.99     |
 * | D+    | 45.0      | 49.99     |
 * | D     | 40.0      | 44.99     |
 * | F     | 0.0       | 39.99     |
 *
 * @property grade The letter grade string
 * @property minScore Minimum score for this grade (inclusive)
 * @property maxScore Maximum score for this grade (inclusive)
 * @property description Human-readable description of performance level
 */
enum class GradeScale(
    val grade: String,
    val minScore: Double,
    val maxScore: Double,
    val description: String
) {
    A("A", 80.0, 100.0, "Excellent"),
    B_PLUS("B+", 70.0, 79.99, "Very Good"),
    B("B", 60.0, 69.99, "Good"),
    C_PLUS("C+", 55.0, 59.99, "Above Average"),
    C("C", 50.0, 54.99, "Average"),
    D_PLUS("D+", 45.0, 49.99, "Below Average"),
    D("D", 40.0, 44.99, "Pass"),
    F("F", 0.0, 39.99, "Fail");

    companion object {

        /**
         * Lambda expression: Maps a score to its corresponding GradeScale entry.
         * Uses firstOrNull with a lambda predicate for matching.
         */
        val scoreToGradeScale: (Double) -> GradeScale = { score ->
            values().firstOrNull { scale ->
                score >= scale.minScore && score <= scale.maxScore
            } ?: F
        }

        /**
         * Returns the GradeScale entry for a given score.
         * Delegates to the scoreToGradeScale lambda.
         */
        fun fromScore(score: Double): GradeScale = scoreToGradeScale(score)

        /**
         * Higher-order function: Finds all grades matching a custom condition.
         * @param predicate Lambda that returns true for grade scales to include
         * @return List of matching GradeScale entries
         */
        fun findGrades(predicate: (GradeScale) -> Boolean): List<GradeScale> {
            return values().filter(predicate)
        }

        /**
         * Returns all passing grades (D and above).
         * Uses lambda with the findGrades higher-order function.
         */
        fun passingGrades(): List<GradeScale> = findGrades { it != F }

        /**
         * Returns the grade description for a given score.
         */
        fun descriptionForScore(score: Double): String = fromScore(score).description
    }
}
