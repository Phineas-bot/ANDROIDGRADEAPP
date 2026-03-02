# Student Grade Calculator App

A professional, production-ready Android application for calculating student grades using Kotlin, MVVM architecture, and Jetpack Compose.

## Features

### Core Functionality
- **Student Input Form**: Enter student name, course name, CA score (0-40), and exam score (0-60)
- **Automatic Grade Calculation**: Calculates total score and assigns letter grades
- **GPA Calculation**: Computes GPA across multiple courses
- **Course Management**: Add, view, and delete courses
- **Data Persistence**: Save course data using Room database

### Grade Scale
- **A (4.0)**: 70-100 points
- **B (3.0)**: 60-69 points
- **C (2.0)**: 50-59 points
- **D (1.0)**: 45-49 points
- **F (0.0)**: 0-44 points

### GPA Remark
- **PASS**: GPA >= 2.0
- **FAIL**: GPA < 2.0

### UI Features
- Modern Material Design 3 interface
- Responsive layout with Jetpack Compose
- Rounded cards and modern spacing
- Color-coded grades for easy visualization
- Input validation with error messages
- Loading indicators for async operations
- Delete confirmations

## Architecture

The app follows **MVVM (Model-View-ViewModel)** pattern with clean architecture principles:

### Project Structure
```
com.example.gradecalculator/
├── data/
│   ├── local/
│   │   ├── CourseDao.kt          # Database access object
│   │   └── GradeDatabase.kt      # Room database
│   └── repository/
│       └── CourseRepository.kt   # Data abstraction layer
├── model/
│   └── Course.kt                 # Data classes
├── utils/
│   └── GradeCalculator.kt       # Business logic
├── viewmodel/
│   └── GradeViewModel.kt        # State management
└── ui/
    ├── MainActivity.kt           # Entry point
    ├── screens/
    │   └── MainScreen.kt        # Main UI screen
    ├── components/
    │   └── Components.kt        # Reusable composables
    └── theme/
        ├── Theme.kt            # Material 3 theme
        └── Typography.kt       # Typography configuration
```

### Architecture Layers

#### 1. **Model Layer** (`model/`)
- `Course`: Entity class for database and app logic
- `CourseResult`: Data class for displaying calculated results
- `StudentSummary`: Data class for GPA summary

#### 2. **Data Layer** (`data/`)
- `CourseDao`: Room DAO for database operations
- `GradeDatabase`: Room database singleton
- `CourseRepository`: Repository pattern for data abstraction

#### 3. **Business Logic** (`utils/`)
- `GradeCalculator`: Utility object with:
  - Score validation methods
  - Grade calculation logic
  - GPA computation
  - Rounding utilities

#### 4. **Presentation Layer**
- **ViewModel** (`GradeViewModel`):
  - Manages UI state using StateFlow
  - Handles user inputs
  - Performs validation
  - Coordinates data operations
  
- **UI Components** (`Composable`):
  - `StudentInputForm`: Input form for new courses
  - `CourseResultCard`: Displays course results
  - `StudentSummaryCard`: Shows GPA summary
  - `ErrorDialog`, `DeleteConfirmDialog`: Dialog components

#### 5. **Theme** (`ui/theme/`)
- Material Design 3 color scheme
- Typography configuration
- Light/Dark theme support

## Technology Stack

### Core
- **Language**: Kotlin 1.9.10
- **Min SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34

### Jetpack
- **Compose**: UI toolkit (1.5.4)
- **LiveData/StateFlow**: Reactive state management
- **Room**: Local database persistence (2.6.1)
- **ViewModel**: State management (2.6.2)

### UI
- **Material Design 3**: Modern design system
- **Compose Material Icons**: Icon library

### Database
- **Room**: Type-safe SQLite wrapper

### Coroutines
- **Kotlinx Coroutines**: Asynchronous operations (1.7.3)

## Installation & Setup

### Prerequisites
- Android Studio 2023.1 or later
- JDK 11 or higher
- Android SDK API 34
- Gradle 8.1.4

### Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/grade-calculator.git
   cd ANDROIDGRADEAPP
   ```

2. **Open in Android Studio**
   - File → Open → Select ANDROIDGRADEAPP folder
   - Wait for Gradle sync to complete

3. **Build the Project**
   ```bash
   ./gradlew build
   ```

4. **Run on Device/Emulator**
   - Click "Run" button in Android Studio
   - Or use command line:
   ```bash
   ./gradlew installDebug
   ```

## Usage Guide

### Adding a Course
1. Enter **Student Name** (e.g., "John Doe")
2. Enter **Course Name** (e.g., "Mathematics")
3. Enter **CA Score** (0-40)
4. Enter **Exam Score** (0-60)
5. Click **Calculate & Save**

### Viewing Results
- Course results appear in a card list below the form
- Each card displays:
  - Course name and student name
  - CA, Exam, and Total scores
  - Letter grade with color coding
  - Grade point (GPA)
  - PASS/FAIL remark

### Managing Courses
- **Delete Course**: Click the delete icon on any course card
- **Clear All**: Click "Clear All Courses" button to delete all courses
- **Confirm Deletion**: Dialog appears for safety confirmation

### Understanding GPA
- GPA is automatically calculated as the average of all grade points
- Appears in "Academic Summary" card
- Shows overall semester performance

## Input Validation

The app validates all inputs:

| Field | Validation |
|-------|-----------|
| Student Name | Cannot be empty |
| Course Name | Cannot be empty |
| CA Score | Must be 0-40, numeric |
| Exam Score | Must be 0-60, numeric |

Invalid inputs show error messages via dialogs.

## Data Persistence

### Room Database
- Stores all courses locally on device
- Courses persist between app sessions
- No server required

### Data Structure
```kotlin
@Entity(tableName = "courses")
data class Course(
    @PrimaryKey val id: String,
    val studentName: String,
    val courseName: String,
    val caScore: Double,
    val examScore: Double,
    val timestamp: Long
)
```

## Code Quality

### Design Patterns Used
- **MVVM**: Separation of concerns
- **Repository**: Data abstraction
- **Singleton**: Database instance
- **Factory**: ViewModel creation
- **StateFlow**: Reactive state

### Best Practices
- ✅ Type-safe data operations
- ✅ Proper null safety with Kotlin
- ✅ Coroutine-based async operations
- ✅ Comprehensive error handling
- ✅ Detailed code comments
- ✅ Material Design 3 compliance
- ✅ Accessibility considerations

## Advanced Features

### Implemented
- ✅ MVVM Architecture with ViewModel
- ✅ Room Database with Dao pattern
- ✅ StateFlow for reactive state
- ✅ Jetpack Compose for modern UI
- ✅ Material Design 3 components
- ✅ Input validation
- ✅ Error dialogs and confirmations
- ✅ Data persistence

### Future Enhancements
- Dark theme toggle with system settings
- Splash screen with animation
- PDF export functionality
- Export to CSV
- Student history and analytics
- Multi-student support with profiles
- Backup and restore data
- Search and filter courses

## Testing

### Unit Tests (Can be added)
```kotlin
class GradeCalculatorTest {
    @Test
    fun testGradeCalculation() {
        val result = GradeCalculator.getGrade(85.0)
        assertEquals("A", result)
    }
    
    @Test
    fun testGPACalculation() {
        val gpa = GradeCalculator.calculateGPA(listOf(4.0, 3.0, 2.0))
        assertEquals(3.0, gpa, 0.01)
    }
}
```

### UI Tests (Can be added with Compose testing)
```kotlin
@get:Rule
val composeTestRule = createComposeRule()

@Test
fun testFormValidation() {
    composeTestRule.setContent {
        MainScreen(viewModel = testViewModel)
    }
    composeTestRule
        .onNodeWithText("Calculate & Save")
        .assertIsNotEnabled()
}
```

## Troubleshooting

### Build Issues

**Error: "Unable to find a matching variant"**
- Ensure minSdk and targetSdk are set correctly
- Clean and rebuild: `./gradlew clean build`

**Error: "Gradle sync failed"**
- Check internet connection
- Sync with offline mode disabled
- Try: `./gradlew --refresh-dependencies`

### Runtime Issues

**Database crashes**
- Ensure Room version is compatible (2.6.1+)
- Check if database is properly initialized in MainActivity

**Layout issues**
- Clear Compose cache: Build → Clean Project
- Rebuild with: `./gradlew assembleDebug`

## Performance Optimizations

- Coroutines for non-blocking operations
- Flow for efficient state updates
- Database queries use indexes via Room
- Lazy loading of course list
- Minimal recomposition in Compose

## Security

- No sensitive data exposed
- Room database is encrypted on modern devices
- Input validation prevents injection attacks
- ProGuard enabled for release builds

## License

MIT License - Feel free to use this project for educational purposes.

## Author

Created as a demonstration of professional Android development with Kotlin and Jetpack Compose.

## Support

For issues, feature requests, or questions:
- Check existing GitHub issues
- Create new issue with detailed information
- Include device info and Android version

## Changelog

### Version 1.0.0 (Initial Release)
- ✅ Core grade calculation functionality
- ✅ MVVM architecture implementation
- ✅ Material Design 3 UI
- ✅ Room database persistence
- ✅ Input validation
- ✅ Course management (CRUD)
- ✅ GPA calculation and display

---

**Happy Grading! 🎓**

