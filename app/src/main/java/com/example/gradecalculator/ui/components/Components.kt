package com.example.gradecalculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gradecalculator.model.CourseResult
import com.example.gradecalculator.model.StudentSummary

/**
 * Composable for student input form
 * Allows users to enter student name, course name, CA score, and exam score
 */
@Composable
fun StudentInputForm(
    studentName: String,
    onStudentNameChange: (String) -> Unit,
    courseName: String,
    onCourseNameChange: (String) -> Unit,
    caScore: String,
    onCAScoreChange: (String) -> Unit,
    examScore: String,
    onExamScoreChange: (String) -> Unit,
    onCalculate: () -> Unit,
    isLoading: Boolean,
    isFormValid: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Add New Course",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Student Name Input
            OutlinedTextField(
                value = studentName,
                onValueChange = onStudentNameChange,
                label = { Text("Student Name") },
                placeholder = { Text("Enter student name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            // Course Name Input
            OutlinedTextField(
                value = courseName,
                onValueChange = onCourseNameChange,
                label = { Text("Course Name") },
                placeholder = { Text("e.g., Mathematics") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            // CA Score Input
            OutlinedTextField(
                value = caScore,
                onValueChange = onCAScoreChange,
                label = { Text("CA Score (0-40)") },
                placeholder = { Text("0-40") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardType = KeyboardType.Number,
                shape = RoundedCornerShape(8.dp)
            )

            // Exam Score Input
            OutlinedTextField(
                value = examScore,
                onValueChange = onExamScoreChange,
                label = { Text("Exam Score (0-60)") },
                placeholder = { Text("0-60") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardType = KeyboardType.Number,
                shape = RoundedCornerShape(8.dp)
            )

            // Calculate Button
            Button(
                onClick = onCalculate,
                enabled = !isLoading && isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (isLoading) "Calculating..." else "Calculate & Save",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Composable for displaying a single course result
 * Shows course name, scores, grade, and GPA
 */
@Composable
fun CourseResultCard(
    result: CourseResult,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradeColor = when (result.grade) {
        "A" -> Color(0xFF4CAF50) // Green
        "B" -> Color(0xFF2196F3) // Blue
        "C" -> Color(0xFFFFC107) // Amber
        "D" -> Color(0xFFFF9800) // Orange
        else -> Color(0xFFF44336)  // Red (F)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with delete button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = result.courseName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Student: ${result.studentName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .background(
                            color = Color(0xFFFFEBEE),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Delete course",
                        tint = Color(0xFFF44336)
                    )
                }
            }

            // Scores section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ScoreBox(
                    label = "CA",
                    score = "${result.totalScore - 60}",
                    modifier = Modifier.weight(1f)
                )
                ScoreBox(
                    label = "Exam",
                    score = "60",
                    modifier = Modifier.weight(1f)
                )
                ScoreBox(
                    label = "Total",
                    score = "${result.totalScore.toInt()}",
                    modifier = Modifier.weight(1f)
                )
            }

            // Grade and GPA section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Grade Box
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = gradeColor.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Grade",
                            style = MaterialTheme.typography.labelSmall,
                            color = gradeColor
                        )
                        Text(
                            text = result.grade,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = gradeColor
                        )
                    }
                }

                // GPA Box
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "GPA",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = String.format("%.2f", result.gradePoint),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Remark Box
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = if (result.remark == "PASS")
                                Color(0xFFE8F5E9)
                            else
                                Color(0xFFFFEBEE),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = result.remark,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (result.remark == "PASS")
                            Color(0xFF2E7D32)
                        else
                            Color(0xFFC62828),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/**
 * Helper composable for displaying score boxes
 */
@Composable
fun ScoreBox(
    label: String,
    score: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = score,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * Composable for displaying student GPA summary
 */
@Composable
fun StudentSummaryCard(
    summary: StudentSummary,
    modifier: Modifier = Modifier
) {
    val summaryColor = if (summary.overallRemark == "PASS")
        Color(0xFF4CAF50)
    else
        Color(0xFFF44336)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = summaryColor.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = androidx.compose.foundation.border(
            width = 2.dp,
            color = summaryColor,
            shape = RoundedCornerShape(12.dp)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Academic Summary",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = summary.studentName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SummaryStatItem(
                    label = "Courses",
                    value = summary.totalCourses.toString(),
                    modifier = Modifier.weight(1f)
                )

                SummaryStatItem(
                    label = "GPA",
                    value = String.format("%.2f", summary.gpa),
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = summaryColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = summary.overallRemark,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = summaryColor,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/**
 * Helper composable for summary statistics
 */
@Composable
fun SummaryStatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * Error dialog for displaying validation errors
 */
@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Error",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK", style = MaterialTheme.typography.labelLarge)
            }
        },
        shape = RoundedCornerShape(12.dp)
    )
}

/**
 * Confirmation dialog for delete actions
 */
@Composable
fun DeleteConfirmDialog(
    courseName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Delete Course",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Are you sure you want to delete '$courseName'?",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                modifier = Modifier
            ) {
                Text("Delete", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(12.dp)
    )
}

