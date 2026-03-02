package com.example.gradecalculator.utils

import com.example.gradecalculator.model.CourseResult

/**
 * Utility class for grade calculations
 * Contains business logic for converting scores to grades and calculating GPA
 */
object GradeCalculator {

    // Grade thresholds and their corresponding points
    private const val GRADE_A_MIN = 70.0
    private const val GRADE_B_MIN = 60.0
    private const val GRADE_C_MIN = 50.0
    private const val GRADE_D_MIN = 45.0

    private const val GRADE_POINT_A = 4.0
    private const val GRADE_POINT_B = 3.0
    private const val GRADE_POINT_C = 2.0
    private const val GRADE_POINT_D = 1.0
    private const val GRADE_POINT_F = 0.0

    private const val GPA_PASS_THRESHOLD = 2.0

    // Maximum scores
    private const val MAX_CA_SCORE = 40.0
    private const val MAX_EXAM_SCORE = 60.0
    private const val MAX_TOTAL_SCORE = 100.0

    /**
     * Validates CA score
     * @param caScore The Continuous Assessment score
     * @return true if valid, false otherwise
     */
    fun isValidCAScore(caScore: Double): Boolean {
        return caScore >= 0 && caScore <= MAX_CA_SCORE
    }

    /**
     * Validates Exam score
     * @param examScore The Exam score
     * @return true if valid, false otherwise
     */
    fun isValidExamScore(examScore: Double): Boolean {
        return examScore >= 0 && examScore <= MAX_EXAM_SCORE
    }

    /**
     * Validates student name
     * @param name The student name
     * @return true if name is not empty
     */
    fun isValidStudentName(name: String): Boolean {
        return name.isNotBlank()
    }

    /**
     * Validates course name
     * @param name The course name
     * @return true if name is not empty
     */
    fun isValidCourseName(name: String): Boolean {
        return name.isNotBlank()
    }

    /**
     * Calculates total score from CA and Exam scores
     * @param caScore Continuous Assessment score (0-40)
     * @param examScore Exam score (0-60)
     * @return Total score (0-100)
     */
    fun calculateTotalScore(caScore: Double, examScore: Double): Double {
        return caScore + examScore
    }

    /**
     * Converts total score to letter grade
     * 70–100 = A
     * 60–69 = B
     * 50–59 = C
     * 45–49 = D
     * 0–44 = F
     *
     * @param totalScore The total score
     * @return Letter grade (A, B, C, D, or F)
     */
    fun getGrade(totalScore: Double): String {
        return when {
            totalScore >= GRADE_A_MIN -> "A"
            totalScore >= GRADE_B_MIN -> "B"
            totalScore >= GRADE_C_MIN -> "C"
            totalScore >= GRADE_D_MIN -> "D"
            else -> "F"
        }
    }

    /**
     * Converts letter grade to grade point
     * A = 4.0, B = 3.0, C = 2.0, D = 1.0, F = 0.0
     *
     * @param grade The letter grade
     * @return Grade point value
     */
    fun getGradePoint(grade: String): Double {
        return when (grade) {
            "A" -> GRADE_POINT_A
            "B" -> GRADE_POINT_B
            "C" -> GRADE_POINT_C
            "D" -> GRADE_POINT_D
            else -> GRADE_POINT_F
        }
    }

    /**
     * Determines remark based on grade point
     * GPA >= 2.0 = PASS
     * GPA < 2.0 = FAIL
     *
     * @param gradePoint The grade point
     * @return PASS or FAIL remark
     */
    fun getRemark(gradePoint: Double): String {
        return if (gradePoint >= GPA_PASS_THRESHOLD) "PASS" else "FAIL"
    }

    /**
     * Calculates GPA from a list of grade points
     * GPA = Sum(GradePoints) / NumberOfCourses
     *
     * @param gradePoints List of grade points
     * @return Calculated GPA (rounded to 2 decimal places)
     */
    fun calculateGPA(gradePoints: List<Double>): Double {
        if (gradePoints.isEmpty()) return 0.0
        val sum = gradePoints.sum()
        val gpa = sum / gradePoints.size
        return String.format("%.2f", gpa).toDouble()
    }

    /**
     * Calculates complete course result from input scores
     *
     * @param studentName Name of the student
     * @param courseName Name of the course
     * @param caScore CA score (0-40)
     * @param examScore Exam score (0-60)
     * @param id Course ID
     * @return CourseResult object with all calculated values
     */
    fun calculateCourseResult(
        studentName: String,
        courseName: String,
        caScore: Double,
        examScore: Double,
        id: String
    ): CourseResult {
        val totalScore = calculateTotalScore(caScore, examScore)
        val grade = getGrade(totalScore)
        val gradePoint = getGradePoint(grade)
        val remark = getRemark(gradePoint)

        return CourseResult(
            id = id,
            studentName = studentName,
            courseName = courseName,
            totalScore = totalScore,
            grade = grade,
            gradePoint = gradePoint,
            remark = remark
        )
    }

    /**
     * Rounds a double value to specified decimal places
     *
     * @param value The value to round
     * @param decimals Number of decimal places
     * @return Rounded value
     */
    fun roundTo(value: Double, decimals: Int): Double {
        return String.format("%.${decimals}f", value).toDouble()
    }
}

