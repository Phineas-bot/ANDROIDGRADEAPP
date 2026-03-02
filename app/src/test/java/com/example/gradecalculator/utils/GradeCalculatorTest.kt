package com.example.gradecalculator.utils

import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for GradeCalculator utility class
 * Tests grade calculation, GPA computation, and validation logic
 */
class GradeCalculatorTest {

    // Grade Calculation Tests

    @Test
    fun testGradeA() {
        val grade = GradeCalculator.getGrade(85.0)
        assertEquals("A", grade)
    }

    @Test
    fun testGradeB() {
        val grade = GradeCalculator.getGrade(65.0)
        assertEquals("B", grade)
    }

    @Test
    fun testGradeC() {
        val grade = GradeCalculator.getGrade(55.0)
        assertEquals("C", grade)
    }

    @Test
    fun testGradeD() {
        val grade = GradeCalculator.getGrade(47.0)
        assertEquals("D", grade)
    }

    @Test
    fun testGradeF() {
        val grade = GradeCalculator.getGrade(35.0)
        assertEquals("F", grade)
    }

    @Test
    fun testGradeBoundaryA() {
        val grade = GradeCalculator.getGrade(70.0)
        assertEquals("A", grade)
    }

    @Test
    fun testGradeBoundaryB() {
        val grade = GradeCalculator.getGrade(60.0)
        assertEquals("B", grade)
    }

    // Grade Point Tests

    @Test
    fun testGradePointA() {
        val point = GradeCalculator.getGradePoint("A")
        assertEquals(4.0, point, 0.0)
    }

    @Test
    fun testGradePointB() {
        val point = GradeCalculator.getGradePoint("B")
        assertEquals(3.0, point, 0.0)
    }

    @Test
    fun testGradePointC() {
        val point = GradeCalculator.getGradePoint("C")
        assertEquals(2.0, point, 0.0)
    }

    @Test
    fun testGradePointD() {
        val point = GradeCalculator.getGradePoint("D")
        assertEquals(1.0, point, 0.0)
    }

    @Test
    fun testGradePointF() {
        val point = GradeCalculator.getGradePoint("F")
        assertEquals(0.0, point, 0.0)
    }

    // Remark Tests

    @Test
    fun testRemarkPass() {
        val remark = GradeCalculator.getRemark(2.5)
        assertEquals("PASS", remark)
    }

    @Test
    fun testRemarkFail() {
        val remark = GradeCalculator.getRemark(1.5)
        assertEquals("FAIL", remark)
    }

    @Test
    fun testRemarkPassBoundary() {
        val remark = GradeCalculator.getRemark(2.0)
        assertEquals("PASS", remark)
    }

    @Test
    fun testRemarkFailBoundary() {
        val remark = GradeCalculator.getRemark(1.99)
        assertEquals("FAIL", remark)
    }

    // GPA Calculation Tests

    @Test
    fun testGPACalculation() {
        val gpa = GradeCalculator.calculateGPA(listOf(4.0, 3.0, 2.0))
        assertEquals(3.0, gpa, 0.01)
    }

    @Test
    fun testGPASingleCourse() {
        val gpa = GradeCalculator.calculateGPA(listOf(4.0))
        assertEquals(4.0, gpa, 0.01)
    }

    @Test
    fun testGPAEmptyList() {
        val gpa = GradeCalculator.calculateGPA(emptyList())
        assertEquals(0.0, gpa, 0.01)
    }

    @Test
    fun testGPAAllF() {
        val gpa = GradeCalculator.calculateGPA(listOf(0.0, 0.0, 0.0))
        assertEquals(0.0, gpa, 0.01)
    }

    @Test
    fun testGPAAllA() {
        val gpa = GradeCalculator.calculateGPA(listOf(4.0, 4.0, 4.0))
        assertEquals(4.0, gpa, 0.01)
    }

    // Score Validation Tests

    @Test
    fun testValidCAScoreMin() {
        assertTrue(GradeCalculator.isValidCAScore(0.0))
    }

    @Test
    fun testValidCAScoreMax() {
        assertTrue(GradeCalculator.isValidCAScore(40.0))
    }

    @Test
    fun testValidCAScoreMid() {
        assertTrue(GradeCalculator.isValidCAScore(20.0))
    }

    @Test
    fun testInvalidCAScoreNegative() {
        assertFalse(GradeCalculator.isValidCAScore(-1.0))
    }

    @Test
    fun testInvalidCAScoreExceeds() {
        assertFalse(GradeCalculator.isValidCAScore(41.0))
    }

    @Test
    fun testValidExamScoreMin() {
        assertTrue(GradeCalculator.isValidExamScore(0.0))
    }

    @Test
    fun testValidExamScoreMax() {
        assertTrue(GradeCalculator.isValidExamScore(60.0))
    }

    @Test
    fun testValidExamScoreMid() {
        assertTrue(GradeCalculator.isValidExamScore(30.0))
    }

    @Test
    fun testInvalidExamScoreNegative() {
        assertFalse(GradeCalculator.isValidExamScore(-1.0))
    }

    @Test
    fun testInvalidExamScoreExceeds() {
        assertFalse(GradeCalculator.isValidExamScore(61.0))
    }

    // Name Validation Tests

    @Test
    fun testValidStudentName() {
        assertTrue(GradeCalculator.isValidStudentName("John Doe"))
    }

    @Test
    fun testInvalidStudentNameEmpty() {
        assertFalse(GradeCalculator.isValidStudentName(""))
    }

    @Test
    fun testInvalidStudentNameBlank() {
        assertFalse(GradeCalculator.isValidStudentName("   "))
    }

    @Test
    fun testValidCourseName() {
        assertTrue(GradeCalculator.isValidCourseName("Mathematics"))
    }

    @Test
    fun testInvalidCourseName() {
        assertFalse(GradeCalculator.isValidCourseName(""))
    }

    // Total Score Calculation Tests

    @Test
    fun testTotalScoreCalculation() {
        val total = GradeCalculator.calculateTotalScore(30.0, 50.0)
        assertEquals(80.0, total, 0.0)
    }

    @Test
    fun testTotalScoreMin() {
        val total = GradeCalculator.calculateTotalScore(0.0, 0.0)
        assertEquals(0.0, total, 0.0)
    }

    @Test
    fun testTotalScoreMax() {
        val total = GradeCalculator.calculateTotalScore(40.0, 60.0)
        assertEquals(100.0, total, 0.0)
    }

    // Complete Course Result Test

    @Test
    fun testCalculateCourseResultA() {
        val result = GradeCalculator.calculateCourseResult(
            "John Doe",
            "Math",
            35.0,
            40.0,
            "1"
        )
        assertEquals("Math", result.courseName)
        assertEquals("John Doe", result.studentName)
        assertEquals(75.0, result.totalScore, 0.0)
        assertEquals("A", result.grade)
        assertEquals(4.0, result.gradePoint, 0.0)
        assertEquals("PASS", result.remark)
    }

    @Test
    fun testCalculateCourseResultF() {
        val result = GradeCalculator.calculateCourseResult(
            "Jane Smith",
            "English",
            10.0,
            20.0,
            "2"
        )
        assertEquals("English", result.courseName)
        assertEquals("Jane Smith", result.studentName)
        assertEquals(30.0, result.totalScore, 0.0)
        assertEquals("F", result.grade)
        assertEquals(0.0, result.gradePoint, 0.0)
        assertEquals("FAIL", result.remark)
    }

    // Rounding Tests

    @Test
    fun testRoundTo2Decimals() {
        val rounded = GradeCalculator.roundTo(3.14159, 2)
        assertEquals(3.14, rounded, 0.01)
    }

    @Test
    fun testRoundTo1Decimal() {
        val rounded = GradeCalculator.roundTo(3.456, 1)
        assertEquals(3.5, rounded, 0.01)
    }

    @Test
    fun testRoundTo0Decimals() {
        val rounded = GradeCalculator.roundTo(3.7, 0)
        assertEquals(4.0, rounded, 0.01)
    }
}

