# Architecture & Implementation Guide

## Project Overview

The Grade Calculator App is a professional Android application demonstrating modern Android development practices using:
- **Kotlin** as the programming language
- **MVVM** architectural pattern
- **Jetpack Compose** for UI
- **Material Design 3** for design system
- **Room Database** for local persistence
- **Coroutines & Flow** for reactive programming

## Architectural Pattern: MVVM

### What is MVVM?

MVVM separates the application into three layers:

1. **Model**: Data and business logic
2. **View**: UI components (Composables)
3. **ViewModel**: Connects Model and View, manages UI state

### Why MVVM?

- ✅ Clear separation of concerns
- ✅ Testability - easy to test business logic separately
- ✅ Reusability - ViewModel survives configuration changes
- ✅ Maintainability - organized code structure
- ✅ Reactivity - automatic UI updates when data changes

### Data Flow in Our App

```
User Input (UI)
    ↓
ViewModel Methods
    ↓
Validation & Business Logic
    ↓
Repository (Data Access)
    ↓
Room Database
    ↓
Flow/StateFlow (State Update)
    ↓
Recompose UI
    ↓
Display Results
```

## Detailed Component Explanation

### 1. MODEL LAYER

#### `Course.kt`

```kotlin
@Entity(tableName = "courses")
data class Course(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val studentName: String,
    val courseName: String,
    val caScore: Double,      // 0-40
    val examScore: Double,    // 0-60
    val timestamp: Long = System.currentTimeMillis()
)
```

**Purpose**: 
- Entity for Room database
- Stores raw input data
- Generated with unique ID and timestamp

**Why UUID?**: Ensures unique identification even if multiple devices create data

#### `CourseResult.kt`

```kotlin
data class CourseResult(
    val id: String,
    val studentName: String,
    val courseName: String,
    val totalScore: Double,    // Calculated: CA + Exam
    val grade: String,         // A, B, C, D, F
    val gradePoint: Double,    // 4.0, 3.0, 2.0, 1.0, 0.0
    val remark: String         // PASS or FAIL
)
```

**Purpose**:
- Display model for UI
- Contains calculated values
- Used for showing results to user
- Different from Course entity (separation of concerns)

#### `StudentSummary.kt`

```kotlin
data class StudentSummary(
    val studentName: String,
    val totalCourses: Int,
    val gpa: Double,           // Average of gradePoints
    val overallRemark: String  // Based on GPA
)
```

**Purpose**:
- Aggregated student statistics
- Shows semester performance
- Calculated from multiple CourseResults

### 2. DATA LAYER

#### `CourseDao.kt` (Data Access Object)

```kotlin
@Dao
interface CourseDao {
    @Insert
    suspend fun insertCourse(course: Course): Long
    
    @Update
    suspend fun updateCourse(course: Course)
    
    @Delete
    suspend fun deleteCourse(course: Course)
    
    @Query("SELECT * FROM courses ORDER BY timestamp DESC")
    fun getAllCourses(): Flow<List<Course>>
}
```

**Why Dao?**
- Type-safe database access
- Compiler checks queries at build time
- Easy to mock for testing

**Suspend Functions**: 
- Run on coroutine scope
- Non-blocking database operations

**Flow Return Type**:
- Reactive - emits data whenever database changes
- Efficient - observers only subscribed to changes

#### `GradeDatabase.kt`

```kotlin
@Database(
    entities = [Course::class],
    version = 1,
    exportSchema = false
)
abstract class GradeDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
    
    companion object {
        @Volatile
        private var INSTANCE: GradeDatabase? = null
        
        fun getDatabase(context: Context): GradeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GradeDatabase::class.java,
                    "grade_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

**Singleton Pattern**:
- Only one database instance per app
- `@Volatile`: Ensures visibility across threads
- `synchronized`: Thread-safe instance creation

**Why Singleton?**
- Prevents multiple database connections
- Efficient resource usage
- Consistent data access

#### `CourseRepository.kt`

```kotlin
class CourseRepository(private val courseDao: CourseDao) {
    fun getAllCourses(): Flow<List<Course>> = courseDao.getAllCourses()
    
    suspend fun insertCourse(course: Course) = courseDao.insertCourse(course)
}
```

**Repository Pattern**:
- Abstraction layer between ViewModel and Database
- Could easily switch database implementation
- Testable - inject mock dao
- Centralizes data logic

### 3. BUSINESS LOGIC LAYER

#### `GradeCalculator.kt`

Contains pure functions for grade calculations:

```kotlin
object GradeCalculator {
    // Validation
    fun isValidCAScore(caScore: Double): Boolean
    fun isValidExamScore(examScore: Double): Boolean
    
    // Calculations
    fun calculateTotalScore(caScore: Double, examScore: Double): Double
    fun getGrade(totalScore: Double): String  // A, B, C, D, F
    fun getGradePoint(grade: String): Double  // 4.0, 3.0, etc.
    fun getRemark(gradePoint: Double): String  // PASS or FAIL
    fun calculateGPA(gradePoints: List<Double>): Double
}
```

**Why Object?**
- Singleton with utility functions
- No need to instantiate
- Consistent behavior across app

**Pure Functions**:
- Same input always produces same output
- No side effects
- Easy to test

### 4. PRESENTATION LAYER

#### `GradeViewModel.kt`

State management using StateFlow:

```kotlin
class GradeViewModel(private val repository: CourseRepository) : ViewModel() {
    // Mutable state (internal)
    private val _studentName = MutableStateFlow("")
    
    // Exposed read-only state (public)
    val studentName: StateFlow<String> = _studentName.asStateFlow()
    
    // Derived flows
    val courseResults: Flow<List<CourseResult>> = 
        allCourses.map { courses ->
            courses.map { course -> 
                GradeCalculator.calculateCourseResult(...) 
            }
        }
}
```

**StateFlow vs MutableStateFlow**:
- MutableStateFlow: Internal, can modify
- StateFlow: Exposed to UI, read-only
- Prevents accidental external modifications

**Derived Flows (map)**:
- Transforms one Flow into another
- Example: Course entities → CourseResults
- Efficient - only processes when source changes

**Lifecycle Safety**:
- ViewModel survives configuration changes
- UI recreated, state preserved
- No memory leaks with Compose

#### State Management Methods

```kotlin
fun calculateAndSaveCourse() {
    // 1. Validate inputs
    if (!validateInputs()) return
    
    // 2. Launch coroutine
    viewModelScope.launch {
        try {
            _isLoading.value = true
            
            // 3. Calculate result
            val result = GradeCalculator.calculateCourseResult(...)
            
            // 4. Save to database
            val course = Course(...)
            repository.insertCourse(course)
            
            // 5. Clear form
            clearForm()
            
        } catch (e: Exception) {
            setError("Error: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }
}
```

**Exception Handling**:
- Try-catch for error recovery
- Error messages shown to user
- Loading state prevents multiple submissions

### 5. UI LAYER

#### Composables (Functional UI Components)

**Key Principle**: UI state flows down, events flow up

```
ViewModel State ↓
    ↓
Composable receives state
    ↓
Composable renders UI
    ↓
User interaction → callback ↑
    ↓
ViewModel handles event
    ↓
State updates
```

#### `StudentInputForm` Composable

```kotlin
@Composable
fun StudentInputForm(
    studentName: String,
    onStudentNameChange: (String) -> Unit,  // Event callback
    // ... other parameters
    onCalculate: () -> Unit                 // Submit callback
) {
    Card { /* UI elements */ }
}
```

**One-way Data Flow**:
- Receives state as parameters
- Provides callbacks for changes
- Doesn't directly modify state

**Button Disabled Logic**:
```kotlin
Button(
    enabled = !isLoading && isFormValid,  // Prevent double-submit
    onClick = onCalculate
)
```

#### Material Design 3 Components Used

1. **Card**: Container with elevation
   - `CardDefaults.cardColors()` for theming
   - `elevation` for shadow

2. **OutlinedTextField**: Text input with border
   - `keyboardType = KeyboardType.Number` for numeric input
   - `label` and `placeholder` for hints

3. **Button**: Primary action
   - `enabled` state for validation
   - `shape = RoundedCornerShape(8.dp)` for modern look

4. **MaterialTheme.colorScheme**: 
   - Access Material colors
   - primary, secondary, error, etc.

5. **Text**: Typography with Material styles
   - `MaterialTheme.typography.headlineSmall`
   - Ensures consistency

## Data Flow Example: Adding a Course

### Step 1: User enters data
```
UI Form
├─ Student Name: "John Doe"
├─ Course Name: "Math"
├─ CA Score: "30"
└─ Exam Score: "50"
```

### Step 2: User clicks Calculate button
```
MainScreen.kt:
onCalculate = viewModel::calculateAndSaveCourse()
```

### Step 3: ViewModel processes
```
GradeViewModel.calculateAndSaveCourse()
├─ 1. validateInputs()
│   ├─ Check studentName not empty ✓
│   ├─ Check courseName not empty ✓
│   ├─ Check CA in 0-40 range ✓
│   └─ Check Exam in 0-60 range ✓
│
├─ 2. Calculate result
│   └─ GradeCalculator.calculateCourseResult()
│       ├─ totalScore = 30 + 50 = 80
│       ├─ grade = "A" (80 >= 70)
│       ├─ gradePoint = 4.0
│       └─ remark = "PASS" (4.0 >= 2.0)
│
├─ 3. Create Course entity
│   └─ Course(
│       studentName = "John Doe",
│       courseName = "Math",
│       caScore = 30.0,
│       examScore = 50.0,
│       id = UUID,
│       timestamp = now
│     )
│
└─ 4. Save to repository
    └─ repository.insertCourse(course)
        └─ courseDao.insertCourse(course)
            └─ Room Database INSERT
```

### Step 4: Database change triggers UI update
```
Room detects INSERT
    ↓
courseDao.getAllCourses() emits new list
    ↓
allCourses Flow updated
    ↓
courseResults Flow transformed via map
    ↓
Compose recomposes with new data
    ↓
CourseResultCard appears
```

### Step 5: Student sees results
```
CourseResultCard displays:
├─ Course: Math
├─ Student: John Doe
├─ Scores:
│  ├─ CA: 30
│  ├─ Exam: 50
│  └─ Total: 80
├─ Grade: A (green)
├─ GPA: 4.0
└─ Remark: PASS

StudentSummaryCard updates:
├─ Courses: 1
├─ GPA: 4.0 (average)
└─ Overall: PASS
```

## Grade Calculation Algorithm

### Score to Grade Mapping
```kotlin
when {
    totalScore >= 70.0 → "A"  // Excellent
    totalScore >= 60.0 → "B"  // Good
    totalScore >= 50.0 → "C"  // Satisfactory
    totalScore >= 45.0 → "D"  // Pass minimum
    else → "F"                // Fail
}
```

### Grade to Grade Point
```kotlin
when (grade) {
    "A" → 4.0
    "B" → 3.0
    "C" → 2.0  // PASS threshold
    "D" → 1.0
    else → 0.0
}
```

### GPA Calculation
```kotlin
GPA = (Grade1 + Grade2 + Grade3 + ...) / NumberOfCourses

Example:
- Math: A = 4.0
- English: B = 3.0
- Science: C = 2.0
- GPA = (4.0 + 3.0 + 2.0) / 3 = 3.0
- Remark = PASS (3.0 >= 2.0)
```

## Validation Logic

### Input Validation Checks

```kotlin
fun validateInputs(): Boolean {
    // 1. Student name not empty
    if (studentName.isBlank()) {
        setError("Invalid student name")
        return false
    }
    
    // 2. Course name not empty
    if (courseName.isBlank()) {
        setError("Invalid course name")
        return false
    }
    
    // 3. Scores not empty
    if (caScore.isEmpty() || examScore.isEmpty()) {
        setError("Enter both scores")
        return false
    }
    
    // 4. Parse and validate ranges
    try {
        val ca = caScore.toDouble()
        val exam = examScore.toDouble()
        
        if (ca < 0 || ca > 40) {
            setError("CA must be 0-40")
            return false
        }
        
        if (exam < 0 || exam > 60) {
            setError("Exam must be 0-60")
            return false
        }
        
        return true
    } catch (e: NumberFormatException) {
        setError("Invalid number format")
        return false
    }
}
```

### Form Validity State

```kotlin
fun isFormValid(): Boolean {
    return studentName.isNotBlank() &&
           courseName.isNotBlank() &&
           caScore.isNotBlank() &&
           examScore.isNotBlank()
}
```

Used for button enabling:
```kotlin
Button(
    enabled = !isLoading && isFormValid,  // Disabled until filled
    onClick = onCalculate
)
```

## Database Schema

### Courses Table
```sql
CREATE TABLE courses (
    id TEXT PRIMARY KEY,
    studentName TEXT NOT NULL,
    courseName TEXT NOT NULL,
    caScore REAL NOT NULL,
    examScore REAL NOT NULL,
    timestamp INTEGER NOT NULL
);
```

### Indexes (Automatic with Room)
- Primary key on `id`
- Scan by timestamp for sorting

### Query Examples

Get all courses ordered by newest first:
```sql
SELECT * FROM courses ORDER BY timestamp DESC
```

Get all courses for a student:
```sql
SELECT * FROM courses WHERE studentName = 'John Doe'
```

## Material Design 3 Implementation

### Color System
- **Primary**: Main brand color (Purple)
- **Secondary**: Supporting color (Purple Gray)
- **Tertiary**: Accent color (Pink)
- **Surface**: Background containers
- **Error**: Error states

### Typography Scale
- **Display**: Titles and headings
- **Headline**: Section headers
- **Title**: Card titles
- **Body**: Main content
- **Label**: Buttons, badges

### Shape System
- All cards use `RoundedCornerShape(12.dp)`
- Input fields use `RoundedCornerShape(8.dp)`
- Consistent corner radius throughout

## Coroutine Usage

### ViewModelScope
```kotlin
viewModelScope.launch {
    // Runs on Main dispatcher
    val data = repository.getData()  // Suspend function
    
    // Update UI state
    _state.value = data
}
```

**Why viewModelScope?**
- Cancels automatically when ViewModel is destroyed
- Prevents memory leaks
- Lifecycle-aware

### Flow vs StateFlow

**Flow**: One-way stream
```kotlin
val courses: Flow<List<Course>> = repository.getAllCourses()
```

**StateFlow**: Stateful, current value
```kotlin
val studentName: StateFlow<String> = _studentName.asStateFlow()

// Can access current value:
val currentName = studentName.value
```

## Testing Strategy (Can be implemented)

### Unit Tests for GradeCalculator
```kotlin
@Test
fun testGradeCalculation() {
    val grade = GradeCalculator.getGrade(75.0)
    assertEquals("A", grade)
}

@Test
fun testGPACalculation() {
    val gpa = GradeCalculator.calculateGPA(listOf(4.0, 3.0, 2.0))
    assertEquals(3.0, gpa, 0.01)
}

@Test
fun testValidation() {
    assertTrue(GradeCalculator.isValidCAScore(30.0))
    assertFalse(GradeCalculator.isValidCAScore(50.0))
}
```

### ViewModel Tests
```kotlin
@Test
fun testCalculateAndSave() = runTest {
    val repository = mockk<CourseRepository>()
    val viewModel = GradeViewModel(repository)
    
    viewModel.updateStudentName("John")
    viewModel.updateCourseName("Math")
    viewModel.updateCAScore("30")
    viewModel.updateExamScore("50")
    
    viewModel.calculateAndSaveCourse()
    
    coVerify { repository.insertCourse(any()) }
}
```

### UI Tests
```kotlin
@Test
fun testFormValidation() = runComposeUiTest {
    setContent {
        MainScreen(viewModel)
    }
    
    onNodeWithText("Calculate & Save").assertIsNotEnabled()
    
    onNodeWithText("Student Name").performTextInput("John")
    onNodeWithText("Course Name").performTextInput("Math")
    onNodeWithText("CA Score (0-40)").performTextInput("30")
    onNodeWithText("Exam Score (0-60)").performTextInput("50")
    
    onNodeWithText("Calculate & Save").assertIsEnabled()
}
```

## Performance Considerations

### Efficient State Updates
- Only relevant composables recompose
- Flow prevents unnecessary database queries
- Lazy loading with LazyColumn

### Database Optimization
- Room handles query optimization
- Indexed primary key
- Efficient CRUD operations

### Memory Management
- Coroutines automatically cleanup with viewModelScope
- No static references to Context
- Proper use of objects vs classes

## Security Best Practices

### Input Validation
- All user inputs validated
- Type safety with Kotlin
- SQL injection prevention (Room)

### Data Storage
- No sensitive data (grades aren't sensitive)
- Room database local storage
- ProGuard obfuscation enabled

### Exception Handling
- Try-catch for database errors
- User-friendly error messages
- No stack traces in UI

---

This architecture ensures the app is:
- ✅ Maintainable - clear structure
- ✅ Testable - decoupled components
- ✅ Scalable - easy to add features
- ✅ Professional - follows Android best practices
- ✅ Performant - efficient state management

