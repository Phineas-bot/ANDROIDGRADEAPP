package com.example.gradecalculator.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.gradecalculator.data.local.GradeDatabase
import com.example.gradecalculator.data.repository.CourseRepository
import com.example.gradecalculator.ui.screens.MainScreen
import com.example.gradecalculator.ui.theme.GradeCalculatorTheme
import com.example.gradecalculator.viewmodel.GradeViewModel
import com.example.gradecalculator.viewmodel.GradeViewModelFactory

/**
 * Main Activity - Entry point for the Grade Calculator Application
 *
 * This activity sets up the Compose UI and initializes the ViewModel
 * with necessary dependencies (Repository and Database)
 *
 * Architecture:
 * - Uses MVVM pattern with ViewModel
 * - Room database for persistence
 * - Jetpack Compose for UI
 * - Material Design 3 theme
 */
class MainActivity : ComponentActivity() {

    private lateinit var viewModel: GradeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database and repository
        val database = GradeDatabase.getDatabase(applicationContext)
        val courseDao = database.courseDao()
        val repository = CourseRepository(courseDao)

        // Initialize ViewModel with factory
        val viewModelFactory = GradeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[GradeViewModel::class.java]

        // Set Compose content
        setContent {
            GradeCalculatorTheme(
                darkTheme = false // Can be made dynamic with system settings
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

