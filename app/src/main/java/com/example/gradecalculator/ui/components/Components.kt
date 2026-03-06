package com.example.gradecalculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gradecalculator.model.Student

// ══════════════════════════════════════════════════════════════════════════════
// StudentInputForm: Manual entry form for adding students
// ══════════════════════════════════════════════════════════════════════════════

/**
 * Composable for the student manual entry form.
 * Accepts input for Student Name, CA Score, and Test Score.
 * Total Score is calculated automatically when the student is added.
 */
@Composable
fun StudentInputForm(
    studentName: String,
    onStudentNameChange: (String) -> Unit,
    caScore: String,
    onCAScoreChange: (String) -> Unit,
    testScore: String,
    onTestScoreChange: (String) -> Unit,
    onAddStudent: () -> Unit,
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
                text = "Add Student",
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

            // Score inputs in a row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // CA Score Input
                OutlinedTextField(
                    value = caScore,
                    onValueChange = onCAScoreChange,
                    label = { Text("CA Score") },
                    placeholder = { Text("0-100") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(8.dp)
                )

                // Test Score Input
                OutlinedTextField(
                    value = testScore,
                    onValueChange = onTestScoreChange,
                    label = { Text("Test Score") },
                    placeholder = { Text("0-100") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            // Auto-calculated total score preview
            if (caScore.isNotBlank() && testScore.isNotBlank()) {
                val ca = caScore.toDoubleOrNull() ?: 0.0
                val test = testScore.toDoubleOrNull() ?: 0.0
                Text(
                    text = "Total Score (auto): ${String.format("%.1f", ca + test)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            // Add Student Button
            Button(
                onClick = onAddStudent,
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Add Student",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// StudentResultCard: Displays a single student's data and grade
// ══════════════════════════════════════════════════════════════════════════════

/**
 * Composable for displaying a single student result card.
 * Shows name, scores, total, and grade (if calculated).
 *
 * @param student The Student data to display
 * @param index Row number (1-based)
 * @param onDelete Lambda callback when delete is pressed
 */
@Composable
fun StudentResultCard(
    student: Student,
    index: Int,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Lambda expression: determine grade display color
    val gradeColor: (String) -> Color = { grade ->
        when (grade) {
            "A" -> Color(0xFF4CAF50)
            "B+" -> Color(0xFF2196F3)
            "B" -> Color(0xFF03A9F4)
            "C+", "C" -> Color(0xFFFFC107)
            "D+", "D" -> Color(0xFFFF9800)
            "F" -> Color(0xFFF44336)
            else -> Color.Gray
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header: Student name + delete button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "#$index  ${student.studentName}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
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
                        contentDescription = "Delete student",
                        tint = Color(0xFFF44336)
                    )
                }
            }

            // Score boxes row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ScoreBox(label = "CA Score", score = String.format("%.1f", student.caScore), modifier = Modifier.weight(1f))
                ScoreBox(label = "Test Score", score = String.format("%.1f", student.testScore), modifier = Modifier.weight(1f))
                ScoreBox(label = "Total", score = String.format("%.1f", student.totalScore), modifier = Modifier.weight(1f))
            }

            // Grade display (only shown when grade is calculated)
            if (student.grade.isNotEmpty()) {
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
                                color = gradeColor(student.grade).copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Grade",
                                style = MaterialTheme.typography.labelSmall,
                                color = gradeColor(student.grade)
                            )
                            Text(
                                text = student.grade,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = gradeColor(student.grade)
                            )
                        }
                    }

                    // Pass/Fail Box
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = if (student.isPassing) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Status",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (student.isPassing) Color(0xFF2E7D32) else Color(0xFFC62828)
                            )
                            Text(
                                text = if (student.isPassing) "PASS" else "FAIL",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (student.isPassing) Color(0xFF2E7D32) else Color(0xFFC62828)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// ScoreBox: Helper composable for displaying labeled score values
// ══════════════════════════════════════════════════════════════════════════════

/**
 * A small box displaying a label and a score value.
 * Reusable component used within StudentResultCard.
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
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
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
