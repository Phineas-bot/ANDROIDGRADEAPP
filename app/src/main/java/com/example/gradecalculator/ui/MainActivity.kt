package com.example.gradecalculator.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gradecalculator.ui.screens.MainScreen
import com.example.gradecalculator.ui.theme.GradeCalculatorTheme
import com.example.gradecalculator.viewmodel.GradeViewModel

/**
 * Main Activity - Entry point for the Grade Calculator Application.
 *
 * Architecture:
 * - MVVM pattern with ViewModel
 * - Jetpack Compose for UI
 * - Material Design 3 theme
 * - OOP service classes for grade calculation, Excel I/O, and PDF generation
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GradeCalculatorTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GradeCalculatorApp()
                }
            }
        }
    }
}

/**
 * Root composable that sets up file pickers (using Android's Storage Access Framework)
 * and passes callbacks to MainScreen.
 *
 * File pickers use ActivityResultContracts which do not require storage permissions.
 */
@Composable
fun GradeCalculatorApp() {
    val viewModel: GradeViewModel = viewModel()
    val context = LocalContext.current

    // ── File Picker Launchers (Storage Access Framework) ────────────────────

    // Import: Opens system file picker to select an Excel file
    val excelImportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { viewModel.importExcel(context, it) }
    }

    // Export Excel: Opens system file picker to choose save location
    val excelExportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    ) { uri ->
        uri?.let { viewModel.exportExcel(context, it) }
    }

    // Export PDF: Opens system file picker to choose save location
    val pdfExportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri ->
        uri?.let { viewModel.exportPdf(context, it) }
    }

    // ── Main Screen with callback lambdas ──────────────────────────────────

    MainScreen(
        viewModel = viewModel,
        onImportExcel = {
            excelImportLauncher.launch(
                arrayOf(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    "application/vnd.ms-excel"
                )
            )
        },
        onExportExcel = {
            excelExportLauncher.launch("student_grades.xlsx")
        },
        onExportPdf = {
            pdfExportLauncher.launch("student_grades_report.pdf")
        }
    )
}
