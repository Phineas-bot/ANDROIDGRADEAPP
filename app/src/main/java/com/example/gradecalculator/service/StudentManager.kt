package com.example.gradecalculator.service

import com.example.gradecalculator.interfaces.StudentProcessor
import com.example.gradecalculator.model.Student

/**
 * Service class that manages a collection of students.
 * Implements the StudentProcessor interface.
 *
 * Demonstrates:
 * - OOP class implementing an interface
 * - Higher-order functions: every processing method accepts lambdas as parameters
 * - Encapsulation: internal mutable list exposed as immutable through interface
 */
class StudentManager : StudentProcessor {

    /** Encapsulated mutable list of students. */
    private val students = mutableListOf<Student>()

    // ── Basic CRUD Operations ──────────────────────────────────────────────────

    override fun addStudent(student: Student) {
        students.add(student)
    }

    /** Adds multiple students at once. */
    fun addStudents(newStudents: List<Student>) {
        students.addAll(newStudents)
    }

    override fun removeStudent(id: String) {
        // Uses lambda predicate with removeAll
        students.removeAll { it.id == id }
    }

    override fun getAllStudents(): List<Student> = students.toList()

    override fun clearAll() {
        students.clear()
    }

    // ── Higher-Order Function Implementations ──────────────────────────────────

    /**
     * Higher-order function: Filters students using a predicate lambda.
     *
     * Example: filterStudents { it.totalScore >= 50.0 }
     *          filterStudents { it.grade == "A" }
     */
    override fun filterStudents(predicate: (Student) -> Boolean): List<Student> {
        return students.filter(predicate)
    }

    /**
     * Higher-order function: Maps each student to a different type using a transform lambda.
     *
     * Example: mapStudents { it.studentName }          → List<String>
     *          mapStudents { it.totalScore }            → List<Double>
     *          mapStudents { it.toSummary() }           → List<String>
     */
    override fun <R> mapStudents(transform: (Student) -> R): List<R> {
        return students.map(transform)
    }

    /**
     * Higher-order function: Performs a side-effect action on each student.
     *
     * Example: forEachStudent { println(it.toSummary()) }
     */
    override fun forEachStudent(action: (Student) -> Unit) {
        students.forEach(action)
    }

    /**
     * Higher-order function: Sorts students using a custom comparator.
     *
     * Example: sortStudents(compareByDescending { it.totalScore })
     *          sortStudents(compareBy { it.studentName })
     */
    override fun sortStudents(comparator: Comparator<Student>): List<Student> {
        return students.sortedWith(comparator)
    }

    /**
     * Higher-order function: Applies a transformation to ALL students in-place.
     * Replaces the internal list with transformed results.
     *
     * Example: updateAll { gradeCalculator.processStudent(it) }
     */
    override fun updateAll(operation: (Student) -> Student): List<Student> {
        val updated = students.map(operation)
        students.clear()
        students.addAll(updated)
        return students.toList()
    }

    // ── Additional Utility Methods ─────────────────────────────────────────────

    /** Replaces all students with a new list. */
    fun replaceAll(newStudents: List<Student>) {
        students.clear()
        students.addAll(newStudents)
    }

    /** Returns the current count of students. */
    fun count(): Int = students.size

    /**
     * Higher-order function: Groups students by a key extracted via lambda.
     *
     * Example: groupBy { it.grade }  → Map<String, List<Student>>
     */
    fun <K> groupStudentsBy(keySelector: (Student) -> K): Map<K, List<Student>> {
        return students.groupBy(keySelector)
    }

    /**
     * Higher-order function: Finds the first student matching a predicate.
     *
     * Example: findStudent { it.studentName == "John" }
     */
    fun findStudent(predicate: (Student) -> Boolean): Student? {
        return students.firstOrNull(predicate)
    }
}
