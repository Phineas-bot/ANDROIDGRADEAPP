# FAQ

## What is this project?

It is a simple Android grade calculator made with Kotlin and Jetpack Compose.

## Does it use a database?

No. The current version keeps student data in memory while the app is open.

## So how is persistence managed?

It is managed in two ways:
- short-term session state through `GradeViewModel` and `StudentManager`,
- long-term manual storage through Excel export and later Excel import.

So the app has session state, but not automatic permanent local persistence.

## What scores can be entered?

- CA: 0 to 40
- Test: 0 to 60

## How are grades calculated?

The total score is `CA + Test`, then the total is matched to the grade scale in `GradeScale.kt`.

## Can the app import Excel files?

Yes. It can read student data from an Excel file.

## Can the app export files?

Yes. It can export to Excel and PDF.

## Will my data still be there after reopening the app?

Not automatically.

If the app is fully closed and reopened, the in-memory student list is lost unless the data was exported before and later imported again.

## Where is the grading logic?

In `service/GradeCalculatorService.kt`.

## Where is the student list managed?

In `service/StudentManager.kt`.

## Where is the app state managed?

In `viewmodel/GradeViewModel.kt`.

## Is the `homework/` folder part of the app?

No. That folder is separate from the Android app.

## What if Gradle sync fails?

Try:

```bash
./gradlew clean
./gradlew build
```

## Which documentation files are left in this repo?

Only these five:
- `README.md`
- `START_HERE.md`
- `ARCHITECTURE_GUIDE.md`
- `TECHNICAL_SPECIFICATIONS.md`
- `FAQ.md`

