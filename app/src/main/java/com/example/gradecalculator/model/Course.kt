package com.example.gradecalculator.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Data class representing a Course with CA and Exam scores
 *
 * @property id Unique identifier for the course
 * @property studentName Name of the student
 * @property courseName Name of the course
 * @property caScore Continuous Assessment score (0-40)
 * @property examScore Exam score (0-60)
 */
@Entity(tableName = "courses")
data class Course(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val studentName: String,
    val courseName: String,
    val caScore: Double,
    val examScore: Double,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Data class for displaying course results with calculated grade
 *
 * @property id Unique identifier
 * @property studentName Name of the student
 * @property courseName Name of the course
 * @property totalScore Total score (CA + Exam)
 * @property grade Letter grade (A, B, C, D, F)
 * @property gradePoint Numerical grade point (4.0, 3.0, 2.0, 1.0, 0.0)
 * @property remark PASS or FAIL based on grade point
 */
data class CourseResult(
    val id: String,
    val studentName: String,
    val courseName: String,
    val totalScore: Double,
    val grade: String,
    val gradePoint: Double,
    val remark: String
)

/**
 * Data class for student summary statistics
 *
 * @property studentName Name of the student
 * @property totalCourses Total number of courses
 * @property gpa Grade Point Average
 * @property overallRemark PASS or FAIL for the entire semester
 */
data class StudentSummary(
    val studentName: String,
    val totalCourses: Int,
    val gpa: Double,
    val overallRemark: String
)

