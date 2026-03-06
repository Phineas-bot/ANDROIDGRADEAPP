package com.example.gradecalculator.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gradecalculator.model.Student
import com.example.gradecalculator.service.ExcelService
import com.example.gradecalculator.service.GradeCalculatorService
import com.example.gradecalculator.service.PdfService
import com.example.gradecalculator.service.StudentManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel managing the state and business logic of the Grade Calculator app.
 *
 * Demonstrates OOP composition: delegates work to service classes:
 * - GradeCalculatorService (implements GradeCalculable interface)
 * - StudentManager (implements StudentProcessor interface)
 * - ExcelService (implements FileImportable + FileExportable interfaces)
 * - PdfService (PDF report generation)
 *
 * Uses lambda expressions and higher-order functions for student processing.
 */
class GradeViewModel : ViewModel() {

    // ── OOP Composition: Service Dependencies ──────────────────────────────────

    private val gradeCalculator = GradeCalculatorService()
    private val studentManager = StudentManager()
    private val excelService = ExcelService()
    private val pdfService = PdfService()

    // ── UI State (Observable flows) ────────────────────────────────────────────

    private val _studentName = MutableStateFlow("")
    val studentName: StateFlow<String> = _studentName.asStateFlow()

    private val _caScore = MutableStateFlow("")
    val caScore: StateFlow<String> = _caScore.asStateFlow()

    private val _testScore = MutableStateFlow("")
    val testScore: StateFlow<String> = _testScore.asStateFlow()

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    private val _gradesCalculated = MutableStateFlow(false)
    val gradesCalculated: StateFlow<Boolean> = _gradesCalculated.asStateFlow()

    // ── Form Input Handlers ────────────────────────────────────────────────────

    fun updateStudentName(name: String) {
        _studentName.value = name
        clearMessages()
    }

    fun updateCAScore(score: String) {
        _caScore.value = score
        clearMessages()
    }

    fun updateTestScore(score: String) {
        _testScore.value = score
        clearMessages()
    }

    // ── Student Management ─────────────────────────────────────────────────────

    /**
     * Adds a student from manual entry.
     * Validates inputs, then auto-calculates total score.
     */
    fun addStudent() {
        val name = _studentName.value.trim()
        val ca = _caScore.value.toDoubleOrNull()
        val test = _testScore.value.toDoubleOrNull()

        // Validation using lambda-based validators in GradeCalculatorService
        when {
            !gradeCalculator.isValidStudentName(name) -> {
                _errorMessage.value = "Please enter a valid student name"
                return
            }
            ca == null || !gradeCalculator.isValidCAScore(ca) -> {
                _errorMessage.value = "Please enter a valid CA score (0-40)"
                return
            }
            test == null || !gradeCalculator.isValidTestScore(test) -> {
                _errorMessage.value = "Please enter a valid Test score (0-60)"
                return
            }
        }

        val student = Student(
            studentName = name,
            caScore = ca!!,
            testScore = test!!,
            totalScore = gradeCalculator.calculateTotalScore(ca, test)
        )

        studentManager.addStudent(student)
        _students.value = studentManager.getAllStudents()
        _gradesCalculated.value = false

        // Clear form fields
        _studentName.value = ""
        _caScore.value = ""
        _testScore.value = ""
        clearMessages()
        _successMessage.value = "Student '${student.studentName}' added successfully"
    }

    /**
     * Removes a student by ID.
     * Uses lambda expression in StudentManager.removeStudent.
     */
    fun removeStudent(id: String) {
        studentManager.removeStudent(id)
        _students.value = studentManager.getAllStudents()
        if (_gradesCalculated.value && _students.value.isNotEmpty()) {
            calculateGrades()
        } else if (_students.value.isEmpty()) {
            _gradesCalculated.value = false
        }
    }

    /** Clears all students from the collection. */
    fun clearAllStudents() {
        studentManager.clearAll()
        _students.value = emptyList()
        _gradesCalculated.value = false
        _successMessage.value = "All students cleared"
    }

    // ── Grade Calculation (Higher-Order Functions) ──────────────────────────────

    /**
     * Calculates grades for all students.
     *
     * Uses higher-order function: StudentManager.updateAll accepts a lambda
     * that transforms each Student (processStudent from GradeCalculatorService).
     *
     * Internally, GradeCalculatorService uses the gradeMapper lambda to
     * convert scores to letter grades via the GradeScale enum.
     */
    fun calculateGrades() {
        if (studentManager.getAllStudents().isEmpty()) {
            _errorMessage.value = "No students to calculate grades for"
            return
        }

        // Higher-order function call: passing processStudent as a lambda to updateAll
        val processedStudents = studentManager.updateAll { student ->
            gradeCalculator.processStudent(student)
        }

        _students.value = processedStudents
        _gradesCalculated.value = true
        _successMessage.value = "Grades calculated for ${processedStudents.size} students"
    }

    // ── File Operations (Interface-based) ──────────────────────────────────────

    /**
     * Imports students from an Excel file.
     * ExcelService implements the FileImportable interface.
     */
    fun importExcel(context: Context, uri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            clearMessages()
            try {
                val importedStudents = withContext(Dispatchers.IO) {
                    excelService.importStudents(context, uri)
                }

                if (importedStudents.isEmpty()) {
                    _errorMessage.value = "No valid student data found in the Excel file"
                } else {
                    studentManager.replaceAll(importedStudents)
                    _students.value = studentManager.getAllStudents()
                    _gradesCalculated.value = false
                    _successMessage.value = "${importedStudents.size} students imported successfully"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error importing Excel: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Exports students to an Excel file with the Grade column.
     * ExcelService implements the FileExportable interface.
     */
    fun exportExcel(context: Context, uri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            clearMessages()
            try {
                val success = withContext(Dispatchers.IO) {
                    excelService.exportStudents(context, _students.value, uri)
                }
                if (success) {
                    _successMessage.value = "Excel file exported successfully"
                } else {
                    _errorMessage.value = "Failed to export Excel file"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error exporting Excel: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Exports students to a PDF report.
     * Uses PdfService for generation.
     */
    fun exportPdf(context: Context, uri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            clearMessages()
            try {
                val success = withContext(Dispatchers.IO) {
                    pdfService.generateReport(context, _students.value, uri)
                }
                if (success) {
                    _successMessage.value = "PDF report generated successfully"
                } else {
                    _errorMessage.value = "Failed to generate PDF report"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error generating PDF: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ── Statistics (Higher-Order Functions) ─────────────────────────────────────

    /**
     * Higher-order function usage: Gets students filtered by a specific grade.
     * Passes a lambda predicate to StudentManager.filterStudents.
     */
    fun getStudentsByGrade(grade: String): List<Student> {
        return studentManager.filterStudents { it.grade == grade }
    }

    /**
     * Higher-order function usage: Computes average score.
     * Uses getStatistic with selector and aggregator lambdas.
     */
    fun getAverageScore(): Double {
        return gradeCalculator.getStatistic(
            students = _students.value,
            selector = { it.totalScore },                                   // lambda: extract totalScore
            aggregator = { scores -> if (scores.isEmpty()) 0.0 else scores.average() }  // lambda: compute average
        )
    }

    /** Counts passing students using a lambda predicate. */
    fun getPassingCount(): Int {
        return gradeCalculator.countStudents(_students.value) { it.isPassing }
    }

    /** Counts failing students using a lambda predicate. */
    fun getFailingCount(): Int {
        return gradeCalculator.countStudents(_students.value) { it.grade == "F" }
    }

    /** Checks if the manual entry form has valid (non-blank) inputs. */
    fun isFormValid(): Boolean {
        return _studentName.value.isNotBlank() &&
                _caScore.value.isNotBlank() &&
                _testScore.value.isNotBlank()
    }

    // ── Message Helpers ────────────────────────────────────────────────────────

    private fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }

    fun dismissError() {
        _errorMessage.value = null
    }

    fun dismissSuccess() {
        _successMessage.value = null
    }
}
