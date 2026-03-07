# Architecture Guide

## Overview

The app is organized in a simple MVVM-style way. It is not a heavy enterprise structure. The main goal is to keep the code easy to follow.

## Package overview

```text
com.example.gradecalculator
├── interfaces
├── model
├── service
├── ui
├── utils
└── viewmodel
```

## Main flow

```text
User input
  -> MainScreen
  -> GradeViewModel
  -> service classes
  -> updated student list
  -> UI recomposes
```

## Layers

### 1. UI layer

Files:
- `ui/MainActivity.kt`
- `ui/screens/MainScreen.kt`
- `ui/components/Components.kt`

Responsibilities:
- show the form,
- show tabs and action buttons,
- show the result list,
- display loading or message states.

### 2. ViewModel layer

File:
- `viewmodel/GradeViewModel.kt`

Responsibilities:
- keep UI state with `StateFlow`,
- validate user input,
- call services,
- update messages and loading states.

### 3. Service layer

Files:
- `service/GradeCalculatorService.kt`
- `service/StudentManager.kt`
- `service/ExcelService.kt`
- `service/PdfService.kt`

Responsibilities:

`GradeCalculatorService`
- validates scores,
- calculates total score,
- maps score to grade.

`StudentManager`
- stores the student list in memory,
- adds, removes, filters, sorts, and updates students.

`ExcelService`
- imports student rows from Excel,
- exports current student results to Excel.

`PdfService`
- creates a PDF report from the student list.

### 4. Model layer

Files:
- `model/Student.kt`
- `model/GradeScale.kt`

`Student` stores student data and grade information.

`GradeScale` is an enum that defines the grading ranges used by the app.

## Kotlin concepts used in the project

The code shows several Kotlin ideas clearly:
- data classes
- enums
- interfaces
- lambdas
- higher-order functions
- extension helpers
- `StateFlow`
- immutable list exposure with internal mutable storage

## Storage note

There is no Room database in the current version.
The student list is stored in memory while the app is open.

More precisely:
- `StudentManager` holds the list in a mutable in-memory collection.
- `GradeViewModel` exposes that list to the UI with `StateFlow`.
- This gives session-level state management, not true persistent storage.
- If the app is fully closed or the process is removed, the in-memory list is lost.
- The only long-term storage in the current app is through file export and later Excel import.
