# Grade Calculator App

This repository contains a simple Android class project for calculating student grades.

## Documentation kept in this repo

 the five documentation files are :

1. `README.md`
2. `START_HERE.md`
3. `ARCHITECTURE_GUIDE.md`
4. `TECHNICAL_SPECIFICATIONS.md`
5. `FAQ.md`

## What the app does

- Add student records manually
- Accept CA score and test score
- Calculate total score and grade
- Show all student results in a list
- Import student records from Excel
- Export results to Excel
- Export a report to PDF
- Clear all records from the current session

## Main tools used

- Kotlin
- Jetpack Compose
- Material 3
- ViewModel + StateFlow
- Coroutines
- Apache POI for Excel files
- Android `PdfDocument` for PDF export

## Project structure

```text
app/src/main/java/com/example/gradecalculator/
├── interfaces/   # contracts for grade and student processing
├── model/        # Student and GradeScale
├── service/      # grade logic, list management, excel, pdf
├── ui/           # activity, screen, composables, theme
├── utils/        # constants and extension helpers
└── viewmodel/    # app state and actions
```

## How the app works

1. The user enters a student name, CA score, and test score.
2. The `GradeViewModel` validates the input.
3. `GradeCalculatorService` calculates the total and grade.
4. `StudentManager` keeps the current list of students.
5. The UI updates automatically through `StateFlow`.

## Grading scale used in this project

| Total score | Grade |
| ----------- | ----- |
| 80 - 100    | A     |
| 70 - 79.99  | B+    |
| 60 - 69.99  | B     |
| 55 - 59.99  | C+    |
| 50 - 54.99  | C     |
| 45 - 49.99  | D+    |
| 40 - 44.99  | D     |
| 0 - 39.99   | F     |

## Quick run steps

1. Open the project in Android Studio.
2. Let Gradle sync finish.
3. Start an emulator or connect a phone.
4. Press Run.

For the fastest setup steps, read [START_HERE.md](START_HERE.md).

## Notes

- This version does not use a local database.
- Student records are kept in memory while the app is open.
- Files can still be saved by exporting to Excel or PDF.
- The `homework/` folder is not part of the Android app.
