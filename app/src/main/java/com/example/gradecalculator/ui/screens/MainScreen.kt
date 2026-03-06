package com.example.gradecalculator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Grading
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gradecalculator.ui.components.StudentInputForm
import com.example.gradecalculator.ui.components.StudentResultCard
import com.example.gradecalculator.viewmodel.GradeViewModel

/**
 * Main screen composable containing the full app UI.
 *
 * Layout:
 * - Title
 * - Tab Row (Manual Entry | Import Excel)
 * - Tab Content (input form or import section)
 * - Action Buttons (Calculate, Export Excel, Export PDF)
 * - Student Results List
 * - Clear All Button
 */
@Composable
fun MainScreen(
    viewModel: GradeViewModel,
    onImportExcel: () -> Unit,
    onExportExcel: () -> Unit,
    onExportPdf: () -> Unit,
    modifier: Modifier = Modifier
) {
    // ── Collect State from ViewModel ───────────────────────────────────────
    val studentName by viewModel.studentName.collectAsState()
    val caScore by viewModel.caScore.collectAsState()
    val testScore by viewModel.testScore.collectAsState()
    val students by viewModel.students.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    val gradesCalculated by viewModel.gradesCalculated.collectAsState()

    // Local UI state
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(modifier = modifier) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // ── App Title ──────────────────────────────────────────────
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Grade Calculator",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Calculate student grades from scores",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // ── Tab Row: Manual Entry | Import Excel ───────────────────
                item {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.primary
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Manual Entry") },
                            icon = { Icon(Icons.Default.Edit, contentDescription = "Manual Entry") }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Import Excel") },
                            icon = { Icon(Icons.Default.Description, contentDescription = "Import Excel") }
                        )
                    }
                }

                // ── Tab Content ────────────────────────────────────────────
                when (selectedTab) {
                    0 -> {
                        // Manual Entry Tab
                        item {
                            StudentInputForm(
                                studentName = studentName,
                                onStudentNameChange = viewModel::updateStudentName,
                                caScore = caScore,
                                onCAScoreChange = viewModel::updateCAScore,
                                testScore = testScore,
                                onTestScoreChange = viewModel::updateTestScore,
                                onAddStudent = viewModel::addStudent,
                                isFormValid = viewModel.isFormValid()
                            )
                        }
                    }
                    1 -> {
                        // Import Excel Tab
                        item {
                            ExcelImportSection(onImportExcel = onImportExcel)
                        }
                    }
                }

                // ── Action Buttons Section ─────────────────────────────────
                if (students.isNotEmpty()) {
                    item {
                        ActionButtonsSection(
                            onCalculateGrades = { viewModel.calculateGrades() },
                            onExportExcel = onExportExcel,
                            onExportPdf = onExportPdf,
                            gradesCalculated = gradesCalculated
                        )
                    }
                }

                // ── Student Results Header ─────────────────────────────────
                if (students.isNotEmpty()) {
                    item {
                        Text(
                            text = "Student Records (${students.size})",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    // ── Student Cards ──────────────────────────────────────
                    itemsIndexed(students) { index, student ->
                        StudentResultCard(
                            student = student,
                            index = index + 1,
                            onDelete = { viewModel.removeStudent(student.id) }
                        )
                    }
                }

                // ── Empty State ────────────────────────────────────────────
                if (students.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Grading,
                                    contentDescription = null,
                                    modifier = Modifier.padding(bottom = 12.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                                Text(
                                    text = "No Students Added Yet",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Add students manually or import from an Excel file",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 8.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                // ── Clear All Button ───────────────────────────────────────
                if (students.isNotEmpty()) {
                    item {
                        OutlinedButton(
                            onClick = { viewModel.clearAllStudents() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFFF44336)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Clear all"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Clear All Students")
                        }
                    }
                }

                // Bottom spacing
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }

            // ── Loading Overlay ────────────────────────────────────────────
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
    }

    // ── Dialogs ────────────────────────────────────────────────────────────────

    // Error Dialog
    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissError() },
            title = {
                Text(
                    text = "Error",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = errorMessage ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissError() }) {
                    Text("OK")
                }
            },
            shape = RoundedCornerShape(12.dp)
        )
    }

    // Success Dialog
    if (successMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissSuccess() },
            title = {
                Text(
                    text = "Success",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            },
            text = {
                Text(
                    text = successMessage ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissSuccess() }) {
                    Text("OK")
                }
            },
            shape = RoundedCornerShape(12.dp)
        )
    }
}

// ══════════════════════════════════════════════════════════════════════════════
// Section Composables
// ══════════════════════════════════════════════════════════════════════════════

/**
 * Section for importing an Excel file.
 * Displays instructions and an upload button.
 */
@Composable
fun ExcelImportSection(
    onImportExcel: () -> Unit,
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Import from Excel",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Upload an Excel file (.xlsx or .xls) with the following columns in order:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            // Column requirements
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    val columns = listOf("1. Student Name", "2. CA Score", "3. Test Score", "4. Total Score")
                    columns.forEach { col ->
                        Text(
                            text = col,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }

            Button(
                onClick = onImportExcel,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.UploadFile,
                    contentDescription = "Upload Excel"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Select Excel File",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Section containing action buttons: Calculate Grades, Export Excel, Export PDF.
 */
@Composable
fun ActionButtonsSection(
    onCalculateGrades: () -> Unit,
    onExportExcel: () -> Unit,
    onExportPdf: () -> Unit,
    gradesCalculated: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Calculate Grades Button
            Button(
                onClick = onCalculateGrades,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Icon(Icons.Default.Grading, contentDescription = "Calculate")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Calculate Grades",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Export buttons row (enabled only when grades are calculated)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Export Excel
                Button(
                    onClick = onExportExcel,
                    enabled = gradesCalculated,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3)
                    )
                ) {
                    Icon(Icons.Default.TableChart, contentDescription = "Export Excel")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Export Excel", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                // Export PDF
                Button(
                    onClick = onExportPdf,
                    enabled = gradesCalculated,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE91E63)
                    )
                ) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = "Export PDF")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Export PDF", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            if (!gradesCalculated) {
                Text(
                    text = "Calculate grades first before exporting",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
