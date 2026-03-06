package com.example.gradecalculator.interfaces

import com.example.gradecalculator.model.Student

/**
 * Interface defining student data processing operations.
 * Demonstrates the use of higher-order functions (functions accepting other functions as parameters).
 *
 * All processing methods use function types (lambdas) as parameters,
 * showcasing Kotlin's functional programming capabilities within an OOP design.
 */
interface StudentProcessor {

    /** Adds a student to the managed collection. */
    fun addStudent(student: Student)

    /** Removes a student by their unique ID. */
    fun removeStudent(id: String)

    /** Returns all managed students as an immutable list. */
    fun getAllStudents(): List<Student>

    /** Clears all students from the collection. */
    fun clearAll()

    /**
     * Higher-order function: Filters students using a predicate lambda.
     * @param predicate Lambda that returns true for students to include
     * @return Filtered list of students
     *
     * Example usage: filterStudents { it.totalScore >= 50.0 }
     */
    fun filterStudents(predicate: (Student) -> Boolean): List<Student>

    /**
     * Higher-order function: Transforms each student into a different type using a mapping function.
     * @param transform Lambda that converts a Student into type R
     * @return List of transformed results
     *
     * Example usage: mapStudents { it.studentName }
     */
    fun <R> mapStudents(transform: (Student) -> R): List<R>

    /**
     * Higher-order function: Performs a side-effect action on each student.
     * @param action Lambda to execute for each student
     *
     * Example usage: forEachStudent { println(it.toSummary()) }
     */
    fun forEachStudent(action: (Student) -> Unit)

    /**
     * Higher-order function: Sorts students using a custom comparator.
     * @param comparator Comparator lambda for ordering students
     * @return Sorted list of students
     *
     * Example usage: sortStudents(compareByDescending { it.totalScore })
     */
    fun sortStudents(comparator: Comparator<Student>): List<Student>

    /**
     * Higher-order function: Applies a batch transformation to all students.
     * @param operation Lambda that processes a Student and returns a modified Student
     * @return List of updated students
     *
     * Example usage: updateAll { gradeCalculator.processStudent(it) }
     */
    fun updateAll(operation: (Student) -> Student): List<Student>
}
