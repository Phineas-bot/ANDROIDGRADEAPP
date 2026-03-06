package com.example.gradecalculator.service

import com.example.gradecalculator.interfaces.GradeCalculable
import com.example.gradecalculator.model.GradeScale
import com.example.gradecalculator.model.Student

/**
 * Service class implementing grade calculation logic.
 * Implements the GradeCalculable interface (OOP interface implementation).
 *
 * Demonstrates:
 * - Lambda expressions for score mapping and validation
 * - Higher-order functions for flexible student processing
 * - OOP class implementing an interface
 */
class GradeCalculatorService : GradeCalculable {

    /**
     * Lambda expression: Maps a total score to a grade string.
     * Can be passed as a parameter to higher-order functions.
     */
    val gradeMapper: (Double) -> String = { score ->
        GradeScale.fromScore(score).grade
    }

    /**
     * Lambda expression: Validates if a value falls within a given range.
     */
    val rangeValidator: (Double, Double, Double) -> Boolean = { value, min, max ->
        value in min..max
    }

    /**
     * Lambda expression: Retrieves the description for a given grade string.
     */
    val gradeDescriptor: (String) -> String = { grade ->
        GradeScale.values().firstOrNull { it.grade == grade }?.description ?: "Unknown"
    }

    // ── Interface Implementations ──────────────────────────────────────────────

    override fun calculateTotalScore(caScore: Double, testScore: Double): Double {
        return caScore + testScore
    }

    override fun calculateGrade(totalScore: Double): String {
        return gradeMapper(totalScore)
    }

    override fun processStudent(student: Student): Student {
        val totalScore = calculateTotalScore(student.caScore, student.testScore)
        val grade = calculateGrade(totalScore)
        return student.copy(totalScore = totalScore, grade = grade)
    }

    /**
     * Higher-order function implementation:
     * Processes a list of students using a custom grading function (lambda parameter).
     * This allows callers to inject their own grading logic.
     */
    override fun processStudents(
        students: List<Student>,
        gradingFunction: (Double) -> String
    ): List<Student> {
        return students.map { student ->
            val totalScore = calculateTotalScore(student.caScore, student.testScore)
            student.copy(totalScore = totalScore, grade = gradingFunction(totalScore))
        }
    }

    // ── Validation Methods (using lambdas) ─────────────────────────────────────

    /** Validates CA score is in 0-40 range using the rangeValidator lambda. */
    fun isValidCAScore(score: Double): Boolean = rangeValidator(score, 0.0, 40.0)

    /** Validates Test score is in 0-60 range using the rangeValidator lambda. */
    fun isValidTestScore(score: Double): Boolean = rangeValidator(score, 0.0, 60.0)

    /** Validates student name is not blank. */
    fun isValidStudentName(name: String): Boolean = name.isNotBlank()

    // ── Higher-Order Statistical Functions ──────────────────────────────────────

    /**
     * Higher-order function: Computes a statistic over a list of students.
     *
     * @param students List of students
     * @param selector Lambda to extract a numeric value from each student
     * @param aggregator Lambda that takes the list of extracted values and returns a result
     * @return The aggregated result
     *
     * Example: getStatistic(students, { it.totalScore }, { it.average() })
     */
    fun <T> getStatistic(
        students: List<Student>,
        selector: (Student) -> Double,
        aggregator: (List<Double>) -> T
    ): T {
        val values = students.map(selector)
        return aggregator(values)
    }

    /**
     * Higher-order function: Counts students matching a predicate.
     *
     * @param students List of students
     * @param predicate Lambda that returns true for students to count
     * @return Count of matching students
     */
    fun countStudents(students: List<Student>, predicate: (Student) -> Boolean): Int {
        return students.count(predicate)
    }
}
