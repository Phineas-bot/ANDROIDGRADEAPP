# Technical Specifications

## Platform

- Language: Kotlin
- Min SDK: 24
- Target SDK: 34
- Compile SDK: 34
- JVM target: 17

## Main libraries

- `androidx.activity:activity-compose`
- `androidx.compose.material3:material3`
- `androidx.lifecycle:lifecycle-viewmodel-ktx`
- `org.jetbrains.kotlinx:kotlinx-coroutines-android`
- `org.apache.poi:poi`
- `org.apache.poi:poi-ooxml`

## Input rules

- Student name must not be blank
- CA score must be between 0 and 40
- Test score must be between 0 and 60

## Grade rules

| Score range | Grade |
|---|---|
| 80 - 100 | A |
| 70 - 79.99 | B+ |
| 60 - 69.99 | B |
| 55 - 59.99 | C+ |
| 50 - 54.99 | C |
| 45 - 49.99 | D+ |
| 40 - 44.99 | D |
| 0 - 39.99 | F |

## File handling

### Excel import

Expected columns:
1. Student Name
2. CA Score
3. Test Score
4. Total Score

The app reads the first sheet of the selected Excel file.

### Excel export

The app exports these columns:
1. Student Name
2. CA Score
3. Test Score
4. Total Score
5. Grade

### PDF export

The app creates a simple PDF table with:
- student name,
- CA score,
- test score,
- total score,
- grade.

## Current limitation

- Data is not stored in a database in the current version.
- Results remain only for the current session unless exported.
