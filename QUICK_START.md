# Quick Start Guide - Grade Calculator App

## 🚀 Getting Started in 5 Minutes

### Prerequisites
- Android Studio 2023.1+
- JDK 11+
- Android SDK 34

### Step 1: Open Project in Android Studio
1. Open Android Studio
2. Click **File** → **Open**
3. Select the `ANDROIDGRADEAPP` folder
4. Wait for Gradle sync to complete (may take 1-2 minutes)

### Step 2: Run the App
1. Connect an Android device OR start an emulator
   - **Emulator**: Tools → Device Manager → Create Virtual Device (API 34)
   - **Device**: Enable Developer Mode (Settings → About → Tap Build Number 7x)

2. Click the **Run** button (green play icon) in Android Studio

3. Select your device and click OK

### Step 3: Try the App
1. **Add a Course**:
   - Student Name: "John Doe"
   - Course Name: "Mathematics"
   - CA Score: 35 (0-40)
   - Exam Score: 50 (0-60)
   - Click "Calculate & Save"

2. **View Results**:
   - See the course card with:
     - Total Score: 85
     - Grade: A
     - GPA: 4.0
     - Remark: PASS

3. **Add More Courses**:
   - Try different scores to see different grades
   - Watch GPA recalculate automatically

4. **Delete Course**:
   - Click the X button on any course card
   - Confirm deletion

## 📁 Project Structure

```
ANDROIDGRADEAPP/
├── app/
│   ├── build.gradle.kts          # Dependencies & build config
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java/com/example/gradecalculator/
│   │   │   │   ├── GradeApplication.kt      # App startup
│   │   │   │   ├── data/
│   │   │   │   │   ├── local/
│   │   │   │   │   │   ├── CourseDao.kt     # Database
│   │   │   │   │   │   └── GradeDatabase.kt
│   │   │   │   │   └── repository/
│   │   │   │   │       └── CourseRepository.kt
│   │   │   │   ├── model/
│   │   │   │   │   └── Course.kt            # Data models
│   │   │   │   ├── viewmodel/
│   │   │   │   │   └── GradeViewModel.kt    # State management
│   │   │   │   ├── utils/
│   │   │   │   │   ├── GradeCalculator.kt   # Business logic
│   │   │   │   │   ├── Constants.kt
│   │   │   │   │   └── Extensions.kt
│   │   │   │   └── ui/
│   │   │   │       ├── MainActivity.kt      # Entry point
│   │   │   │       ├── screens/
│   │   │   │       │   └── MainScreen.kt
│   │   │   │       ├── components/
│   │   │   │       │   └── Components.kt
│   │   │   │       └── theme/
│   │   │   │           ├── Theme.kt
│   │   │   │           └── Typography.kt
│   │   │   └── res/
│   │   │       ├── values/
│   │   │       │   ├── colors.xml
│   │   │       │   ├── strings.xml
│   │   │       │   └── themes.xml
│   │   │       └── xml/
│   │   │           ├── backup_rules.xml
│   │   │           └── data_extraction_rules.xml
│   │   └── test/
│   │       └── GradeCalculatorTest.kt       # Unit tests
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
├── README.md                    # Full documentation
├── ARCHITECTURE_GUIDE.md        # Detailed architecture
├── QUICK_START.md              # This file
└── .gitignore

```

## 🔧 Common Tasks

### Clean Build
```bash
./gradlew clean build
```

### Run Tests
```bash
./gradlew test
```

### Install Debug APK
```bash
./gradlew installDebug
```

### Build Release APK
```bash
./gradlew assembleRelease
```

## ⚡ Grade Calculation Reference

| Total Score | Grade | Grade Point | Remark |
|------------|-------|-------------|--------|
| 70-100    | A     | 4.0         | PASS   |
| 60-69     | B     | 3.0         | PASS   |
| 50-59     | C     | 2.0         | PASS   |
| 45-49     | D     | 1.0         | PASS   |
| 0-44      | F     | 0.0         | FAIL   |

**GPA Remark**: 
- GPA ≥ 2.0 = **PASS**
- GPA < 2.0 = **FAIL**

## 📊 Example Scenarios

### Scenario 1: Good Performance
```
Student: John
Course 1: Math - CA: 35, Exam: 45 → Total: 80 → Grade: A (4.0)
Course 2: English - CA: 32, Exam: 40 → Total: 72 → Grade: A (4.0)
Course 3: Science - CA: 28, Exam: 35 → Total: 63 → Grade: B (3.0)

GPA = (4.0 + 4.0 + 3.0) / 3 = 3.67 → PASS ✅
```

### Scenario 2: Average Performance
```
Student: Jane
Course 1: Math - CA: 25, Exam: 30 → Total: 55 → Grade: C (2.0)
Course 2: English - CA: 20, Exam: 25 → Total: 45 → Grade: D (1.0)

GPA = (2.0 + 1.0) / 2 = 1.5 → FAIL ❌
```

### Scenario 3: Mixed Performance
```
Student: Bob
Course 1: Math - CA: 38, Exam: 55 → Total: 93 → Grade: A (4.0)
Course 2: History - CA: 18, Exam: 15 → Total: 33 → Grade: F (0.0)

GPA = (4.0 + 0.0) / 2 = 2.0 → PASS ✅
```

## 🎨 UI Components Guide

### Input Form
- **Student Name**: Any text (e.g., "John Doe")
- **Course Name**: Any text (e.g., "Mathematics")
- **CA Score**: 0-40 (Continuous Assessment)
- **Exam Score**: 0-60 (Final Exam)
- Button disabled until all fields filled

### Course Result Card
- Shows course info
- Displays CA, Exam, Total scores
- Color-coded grade (A=green, B=blue, C=yellow, D=orange, F=red)
- Delete button (X icon)

### Academic Summary Card
- Shows student name
- Total number of courses
- Overall GPA
- Overall remark (PASS/FAIL with color)

## 🐛 Troubleshooting

### Build Fails
```bash
# Clear gradle cache
./gradlew clean

# Refresh dependencies
./gradlew --refresh-dependencies

# Rebuild
./gradlew build
```

### App Crashes on Launch
- Ensure AndroidManifest.xml is correct
- Check that MainActivity exists
- Try: Build → Clean Project → Run

### Database Not Persisting
- App restarts should keep data
- To reset: Uninstall app → Reinstall

### No Data After Adding Course
- Check logcat for errors: View → Tool Windows → Logcat
- Ensure database initialization in onCreate

## 📝 Code Highlights

### Grade Calculation Logic
```kotlin
// In GradeCalculator.kt
fun calculateTotalScore(caScore: Double, examScore: Double): Double {
    return caScore + examScore  // 0-100
}

fun getGrade(totalScore: Double): String {
    return when {
        totalScore >= 70.0 → "A"
        totalScore >= 60.0 → "B"
        totalScore >= 50.0 → "C"
        totalScore >= 45.0 → "D"
        else → "F"
    }
}

fun calculateGPA(gradePoints: List<Double>): Double {
    return gradePoints.sum() / gradePoints.size
}
```

### ViewModel State Management
```kotlin
// In GradeViewModel.kt
val studentName: StateFlow<String> = _studentName.asStateFlow()

fun updateStudentName(name: String) {
    _studentName.value = name
}

fun calculateAndSaveCourse() {
    viewModelScope.launch {
        // Validate → Calculate → Save → Update UI
    }
}
```

### Composable UI
```kotlin
// In Components.kt
@Composable
fun StudentInputForm(
    studentName: String,
    onStudentNameChange: (String) -> Unit,
    // ... event callbacks
) {
    Card {
        OutlinedTextField(
            value = studentName,
            onValueChange = onStudentNameChange
        )
        Button(onClick = onCalculate)
    }
}
```

## 🔐 Input Validation

All inputs are validated before processing:
- ✅ Student name not empty
- ✅ Course name not empty
- ✅ CA score between 0-40
- ✅ Exam score between 0-60
- ✅ Numeric values only

Invalid inputs show error dialogs.

## 💾 Data Persistence

### What's Saved
- Student name
- Course name
- CA and Exam scores
- Creation timestamp

### Storage
- Local SQLite database (Room)
- Survives app restart
- No internet required

### Clear Data
- Click "Clear All Courses" button
- Or uninstall app

## 🎯 Next Steps

1. **Customize**: Modify colors in `ui/theme/Theme.kt`
2. **Extend**: Add more features (export, analytics, etc.)
3. **Test**: Run unit tests with `./gradlew test`
4. **Release**: Build APK with `./gradlew assembleRelease`
5. **Deploy**: Upload to Google Play Store

## 📚 Additional Resources

- **Full Documentation**: See `README.md`
- **Architecture Deep Dive**: See `ARCHITECTURE_GUIDE.md`
- **Android Documentation**: https://developer.android.com
- **Kotlin Documentation**: https://kotlinlang.org

## ❓ FAQs

**Q: How do I run on a physical device?**
A: Enable USB Debugging in Developer Options, connect device, click Run

**Q: Can I use this with different grading scales?**
A: Yes! Modify grade boundaries in `GradeCalculator.kt`

**Q: How do I add a dark theme?**
A: Set `darkTheme = true` in MainActivity's `GradeCalculatorTheme()`

**Q: Can I export grades as PDF?**
A: Not yet, but can be added as a feature using a PDF library

**Q: How do I backup student data?**
A: Current version stores locally. Add cloud sync for backup.

---

**Need help?** Check the Architecture Guide for detailed explanations of each component.

**Happy Coding! 🎓**

