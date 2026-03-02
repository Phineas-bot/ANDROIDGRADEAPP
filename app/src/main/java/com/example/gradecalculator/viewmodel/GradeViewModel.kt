package com.example.gradecalculator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gradecalculator.data.repository.CourseRepository
import com.example.gradecalculator.model.Course
import com.example.gradecalculator.model.CourseResult
import com.example.gradecalculator.model.StudentSummary
import com.example.gradecalculator.utils.GradeCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ViewModel for managing grade calculation and course data
 * Handles all business logic and state management for the UI
 */
class GradeViewModel(private val repository: CourseRepository) : ViewModel() {

    // Form input state
    private val _studentName = MutableStateFlow("")
    val studentName: StateFlow<String> = _studentName.asStateFlow()

    private val _courseName = MutableStateFlow("")
    val courseName: StateFlow<String> = _courseName.asStateFlow()

    private val _caScore = MutableStateFlow("")
    val caScore: StateFlow<String> = _caScore.asStateFlow()

    private val _examScore = MutableStateFlow("")
    val examScore: StateFlow<String> = _examScore.asStateFlow()

    // Current course result
    private val _courseResult = MutableStateFlow<CourseResult?>(null)
    val courseResult: StateFlow<CourseResult?> = _courseResult.asStateFlow()

    // Error state
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // All courses as Flow
    val allCourses: Flow<List<Course>> = repository.getAllCourses()

    // Course results (calculated from courses)
    val courseResults: Flow<List<CourseResult>> = allCourses.map { courses ->
        courses.map { course ->
            GradeCalculator.calculateCourseResult(
                course.studentName,
                course.courseName,
                course.caScore,
                course.examScore,
                course.id
            )
        }
    }

    // Student summary (GPA and remark)
    val studentSummary: Flow<StudentSummary?> = courseResults.map { results ->
        if (results.isEmpty()) {
            null
        } else {
            val studentName = results.firstOrNull()?.studentName ?: ""
            val gradePoints = results.map { it.gradePoint }
            val gpa = GradeCalculator.calculateGPA(gradePoints)
            val overallRemark = GradeCalculator.getRemark(gpa)

            StudentSummary(
                studentName = studentName,
                totalCourses = results.size,
                gpa = gpa,
                overallRemark = overallRemark
            )
        }
    }

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /**
     * Updates student name input
     */
    fun updateStudentName(name: String) {
        _studentName.value = name
        clearError()
    }

    /**
     * Updates course name input
     */
    fun updateCourseName(name: String) {
        _courseName.value = name
        clearError()
    }

    /**
     * Updates CA score input
     */
    fun updateCAScore(score: String) {
        _caScore.value = score
        clearError()
    }

    /**
     * Updates exam score input
     */
    fun updateExamScore(score: String) {
        _examScore.value = score
        clearError()
    }

    /**
     * Clears error message
     */
    private fun clearError() {
        _errorMessage.value = null
    }

    /**
     * Sets error message
     */
    private fun setError(message: String) {
        _errorMessage.value = message
    }

    /**
     * Validates all inputs
     * @return true if all inputs are valid
     */
    private fun validateInputs(): Boolean {
        val studentName = _studentName.value
        val courseName = _courseName.value
        val caScore = _caScore.value
        val examScore = _examScore.value

        return when {
            !GradeCalculator.isValidStudentName(studentName) -> {
                setError("Please enter a valid student name")
                false
            }
            !GradeCalculator.isValidCourseName(courseName) -> {
                setError("Please enter a valid course name")
                false
            }
            caScore.isEmpty() || examScore.isEmpty() -> {
                setError("Please enter both CA and Exam scores")
                false
            }
            else -> {
                try {
                    val ca = caScore.toDouble()
                    val exam = examScore.toDouble()

                    when {
                        !GradeCalculator.isValidCAScore(ca) -> {
                            setError("CA score must be between 0 and 40")
                            false
                        }
                        !GradeCalculator.isValidExamScore(exam) -> {
                            setError("Exam score must be between 0 and 60")
                            false
                        }
                        else -> true
                    }
                } catch (e: NumberFormatException) {
                    setError("Please enter valid numeric scores")
                    false
                }
            }
        }
    }

    /**
     * Calculates and saves a new course result
     */
    fun calculateAndSaveCourse() {
        if (!validateInputs()) return

        viewModelScope.launch {
            try {
                _isLoading.value = true

                val ca = _caScore.value.toDouble()
                val exam = _examScore.value.toDouble()

                val courseResult = GradeCalculator.calculateCourseResult(
                    _studentName.value,
                    _courseName.value,
                    ca,
                    exam,
                    ""  // ID will be generated
                )

                _courseResult.value = courseResult

                // Save to database
                val course = Course(
                    studentName = _studentName.value,
                    courseName = _courseName.value,
                    caScore = ca,
                    examScore = exam
                )

                repository.insertCourse(course)

                // Clear form after successful save
                clearForm()

                _isLoading.value = false
            } catch (e: Exception) {
                setError("Error: ${e.message}")
                _isLoading.value = false
            }
        }
    }

    /**
     * Clears the input form
     */
    fun clearForm() {
        _studentName.value = ""
        _courseName.value = ""
        _caScore.value = ""
        _examScore.value = ""
        _courseResult.value = null
        clearError()
    }

    /**
     * Deletes a course
     */
    fun deleteCourse(course: Course) {
        viewModelScope.launch {
            try {
                repository.deleteCourse(course)
            } catch (e: Exception) {
                setError("Error deleting course: ${e.message}")
            }
        }
    }

    /**
     * Deletes all courses
     */
    fun deleteAllCourses() {
        viewModelScope.launch {
            try {
                repository.deleteAllCourses()
                clearForm()
            } catch (e: Exception) {
                setError("Error deleting courses: ${e.message}")
            }
        }
    }

    /**
     * Checks if form is filled with valid data for calculation
     */
    fun isFormValid(): Boolean {
        return _studentName.value.isNotBlank() &&
                _courseName.value.isNotBlank() &&
                _caScore.value.isNotBlank() &&
                _examScore.value.isNotBlank()
    }
}

/**
 * ViewModelFactory for creating GradeViewModel instances with repository dependency
 */
class GradeViewModelFactory(private val repository: CourseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GradeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GradeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

