package com.example.gradecalculator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gradecalculator.model.Course
import com.example.gradecalculator.ui.components.CourseResultCard
import com.example.gradecalculator.ui.components.DeleteConfirmDialog
import com.example.gradecalculator.ui.components.ErrorDialog
import com.example.gradecalculator.ui.components.StudentInputForm
import com.example.gradecalculator.ui.components.StudentSummaryCard
import com.example.gradecalculator.viewmodel.GradeViewModel

/**
 * Main screen composable
 * Displays the input form and list of courses with their results
 */
@Composable
fun MainScreen(
    viewModel: GradeViewModel,
    modifier: Modifier = Modifier
) {
    // Collect state from ViewModel
    val studentName by viewModel.studentName.collectAsState()
    val courseName by viewModel.courseName.collectAsState()
    val caScore by viewModel.caScore.collectAsState()
    val examScore by viewModel.examScore.collectAsState()
    val courseResult by viewModel.courseResult.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val courseResults by viewModel.courseResults.collectAsState(initial = emptyList())
    val studentSummary by viewModel.studentSummary.collectAsState(initial = null)
    val allCourses by viewModel.allCourses.collectAsState(initial = emptyList())

    // Dialog states
    var showErrorDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf<Course?>(null) }

    // Show error dialog when error message is set
    if (errorMessage != null && !showErrorDialog) {
        showErrorDialog = true
    }

    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Input Form
            item {
                StudentInputForm(
                    studentName = studentName,
                    onStudentNameChange = viewModel::updateStudentName,
                    courseName = courseName,
                    onCourseNameChange = viewModel::updateCourseName,
                    caScore = caScore,
                    onCAScoreChange = viewModel::updateCAScore,
                    examScore = examScore,
                    onExamScoreChange = viewModel::updateExamScore,
                    onCalculate = viewModel::calculateAndSaveCourse,
                    isLoading = isLoading,
                    isFormValid = viewModel.isFormValid()
                )
            }

            // Course Results Section
            if (courseResults.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Course Results",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }

                items(courseResults) { result ->
                    CourseResultCard(
                        result = result,
                        onDelete = {
                            // Find corresponding course and prepare for deletion
                            val courseToDelete = allCourses.find { it.id == result.id }
                            if (courseToDelete != null) {
                                showDeleteConfirmDialog = courseToDelete
                            }
                        }
                    )
                }
            }

            // Student Summary Section
            if (studentSummary != null) {
                item {
                    StudentSummaryCard(summary = studentSummary!!)
                }
            }

            // Empty State
            if (courseResults.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "No Courses Added Yet",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Add your first course to get started!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 8.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Clear All Button (shown when there are courses)
            if (courseResults.isNotEmpty()) {
                item {
                    Button(
                        onClick = { viewModel.deleteAllCourses() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete all"
                        )
                        Text(
                            text = "Clear All Courses",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }

            // Bottom padding
            item {
                Box(modifier = Modifier.padding(bottom = 16.dp))
            }
        }
    }

    // Error Dialog
    if (showErrorDialog && errorMessage != null) {
        ErrorDialog(
            message = errorMessage!!,
            onDismiss = {
                showErrorDialog = false
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmDialog != null) {
        DeleteConfirmDialog(
            courseName = showDeleteConfirmDialog!!.courseName,
            onConfirm = {
                viewModel.deleteCourse(showDeleteConfirmDialog!!)
                showDeleteConfirmDialog = null
            },
            onDismiss = {
                showDeleteConfirmDialog = null
            }
        )
    }

    // Loading indicator overlay
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

