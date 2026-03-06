package com.example.gradecalculator.service

import android.content.Context
import android.net.Uri
import com.example.gradecalculator.interfaces.FileExportable
import com.example.gradecalculator.interfaces.FileImportable
import com.example.gradecalculator.model.Student
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook

/**
 * Service class for Excel file import and export operations.
 * Implements both FileImportable and FileExportable interfaces.
 *
 * Demonstrates:
 * - OOP class implementing multiple interfaces
 * - Lambda expressions for cell value extraction
 * - Functional processing of spreadsheet rows
 */
class ExcelService : FileImportable, FileExportable {

    /**
     * Lambda expression: Safely extracts a numeric value from an Excel cell.
     * Handles different cell types gracefully.
     */
    private val getNumericValue: (org.apache.poi.ss.usermodel.Cell?) -> Double = { cell ->
        when (cell?.cellType) {
            CellType.NUMERIC -> cell.numericCellValue
            CellType.STRING -> cell.stringCellValue.trim().toDoubleOrNull() ?: 0.0
            CellType.FORMULA -> try { cell.numericCellValue } catch (_: Exception) { 0.0 }
            else -> 0.0
        }
    }

    /**
     * Lambda expression: Safely extracts a string value from an Excel cell.
     * Handles different cell types gracefully.
     */
    private val getStringValue: (org.apache.poi.ss.usermodel.Cell?) -> String = { cell ->
        when (cell?.cellType) {
            CellType.STRING -> cell.stringCellValue.trim()
            CellType.NUMERIC -> cell.numericCellValue.toInt().toString()
            CellType.FORMULA -> try { cell.stringCellValue.trim() } catch (_: Exception) { "" }
            else -> ""
        }
    }

    // ── FileImportable Implementation ──────────────────────────────────────────

    /**
     * Imports students from an Excel file (.xls or .xlsx).
     * Expected columns in order: Student Name, CA Score, Test Score, Total Score
     *
     * Uses WorkbookFactory.create() which auto-detects both .xls and .xlsx formats.
     * Applies lambda expressions for cell value extraction.
     */
    override fun importStudents(context: Context, uri: Uri): List<Student> {
        val students = mutableListOf<Student>()

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val workbook = WorkbookFactory.create(inputStream)
            val sheet = workbook.getSheetAt(0)

            // Skip header row (index 0), process data rows using functional style
            for (rowIndex in 1..sheet.lastRowNum) {
                val row = sheet.getRow(rowIndex) ?: continue

                // Use lambda expressions to extract cell values
                val studentName = getStringValue(row.getCell(0))
                val caScore = getNumericValue(row.getCell(1))
                val testScore = getNumericValue(row.getCell(2))
                val totalScore = getNumericValue(row.getCell(3))

                // Only add students with valid names
                if (studentName.isNotBlank()) {
                    students.add(
                        Student(
                            studentName = studentName,
                            caScore = caScore,
                            testScore = testScore,
                            totalScore = if (totalScore > 0) totalScore else caScore + testScore
                        )
                    )
                }
            }
            workbook.close()
        }

        return students
    }

    // ── FileExportable Implementation ──────────────────────────────────────────

    /**
     * Exports students to an Excel file (.xlsx) with the Grade column added.
     * Output columns: Student Name, CA Score, Test Score, Total Score, Grade
     *
     * Uses lambda expressions (forEachIndexed) for populating rows.
     */
    override fun exportStudents(context: Context, students: List<Student>, uri: Uri): Boolean {
        return try {
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Student Grades")

            // Create header cell style
            val headerStyle = workbook.createCellStyle().apply {
                val font = workbook.createFont().apply {
                    bold = true
                    fontHeightInPoints = 12
                }
                setFont(font)
            }

            // Create header row
            val headers = listOf("Student Name", "CA Score", "Test Score", "Total Score", "Grade")
            val headerRow = sheet.createRow(0)

            // Lambda: Populate header cells using forEachIndexed
            headers.forEachIndexed { index, title ->
                headerRow.createCell(index).apply {
                    setCellValue(title)
                    cellStyle = headerStyle
                }
            }

            // Lambda: Populate data rows using forEachIndexed
            students.forEachIndexed { index, student ->
                val row = sheet.createRow(index + 1)
                row.createCell(0).setCellValue(student.studentName)
                row.createCell(1).setCellValue(student.caScore)
                row.createCell(2).setCellValue(student.testScore)
                row.createCell(3).setCellValue(student.totalScore)
                row.createCell(4).setCellValue(student.grade)
            }

            // Auto-size all columns for readability
            headers.indices.forEach { sheet.autoSizeColumn(it) }

            // Write workbook to the output URI
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                workbook.write(outputStream)
            }
            workbook.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
