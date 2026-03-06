package com.example.gradecalculator.interfaces

import com.example.gradecalculator.model.Student

/**
 * Interface defining grade calculation behavior.
 * Demonstrates OOP interface design principle.
 *
 * Uses higher-order function signatures to allow custom grading logic injection.
 */
interface GradeCalculable {

    /**
     * Calculates the total score from CA and Test scores.
     * @param caScore Continuous Assessment score
     * @param testScore Test score
     * @return The total score
     */
    fun calculateTotalScore(caScore: Double, testScore: Double): Double

    /**
     * Determines the letter grade for a given total score.
     * @param totalScore The total score
     * @return Grade string (e.g., "A", "B+", "C")
     */
    fun calculateGrade(totalScore: Double): String

    /**
     * Processes a single student: computes total score and assigns grade.
     * @param student The student to process
     * @return A new Student with totalScore and grade computed
     */
    fun processStudent(student: Student): Student

    /**
     * Higher-order function: Processes a list of students using a custom grading lambda.
     * The caller can inject their own grading logic as a function parameter.
     *
     * @param students List of students to process
     * @param gradingFunction Lambda that maps a total score (Double) to a grade (String)
     * @return List of students with grades assigned using the provided function
     */
    fun processStudents(students: List<Student>, gradingFunction: (Double) -> String): List<Student>
}
