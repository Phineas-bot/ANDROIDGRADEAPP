package com.example.gradecalculator.service

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import com.example.gradecalculator.model.Student

/**
 * Service class for generating PDF reports containing student grade data.
 * Uses Android's built-in PdfDocument API (no external dependencies).
 *
 * Demonstrates:
 * - OOP class with single responsibility (PDF generation)
 * - Lambda expressions for Paint factory methods and color mapping
 * - Functional processing of student data for layout
 */
class PdfService {

    // ── Lambda Expressions: Paint Factory Methods ──────────────────────────────

    /** Lambda: Creates Paint configured for the document title. */
    private val createTitlePaint: () -> Paint = {
        Paint().apply {
            color = Color.BLACK
            textSize = 24f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
    }

    /** Lambda: Creates Paint configured for table header text (white on colored bg). */
    private val createHeaderPaint: () -> Paint = {
        Paint().apply {
            color = Color.WHITE
            textSize = 13f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
    }

    /** Lambda: Creates Paint configured for normal body text. */
    private val createBodyPaint: () -> Paint = {
        Paint().apply {
            color = Color.BLACK
            textSize = 11f
            typeface = Typeface.DEFAULT
            isAntiAlias = true
        }
    }

    /** Lambda: Creates Paint for table grid lines. */
    private val createLinePaint: () -> Paint = {
        Paint().apply {
            color = Color.LTGRAY
            strokeWidth = 1f
            style = Paint.Style.STROKE
        }
    }

    /** Lambda: Maps a grade string to its display color. */
    private val gradeColorMapper: (String) -> Int = { grade ->
        when (grade) {
            "A" -> Color.rgb(46, 125, 50)
            "B+" -> Color.rgb(33, 150, 243)
            "B" -> Color.rgb(30, 136, 229)
            "C+", "C" -> Color.rgb(255, 152, 0)
            "D+", "D" -> Color.rgb(255, 87, 34)
            "F" -> Color.rgb(198, 40, 40)
            else -> Color.BLACK
        }
    }

    // ── PDF Generation ─────────────────────────────────────────────────────────

    /**
     * Generates a PDF report containing all student data with grades.
     *
     * @param context Android context for content resolver
     * @param students List of students with calculated grades
     * @param uri URI where the PDF file will be written
     * @return true if PDF generation was successful
     */
    fun generateReport(context: Context, students: List<Student>, uri: Uri): Boolean {
        return try {
            val document = PdfDocument()
            val pageWidth = 595   // A4 width in points
            val pageHeight = 842  // A4 height in points

            // Initialize paints using lambda factory methods
            val titlePaint = createTitlePaint()
            val headerPaint = createHeaderPaint()
            val bodyPaint = createBodyPaint()
            val linePaint = createLinePaint()

            val headerBgPaint = Paint().apply {
                color = Color.rgb(103, 80, 164) // Material Purple
                style = Paint.Style.FILL
            }

            val altRowPaint = Paint().apply {
                color = Color.rgb(245, 245, 250)
                style = Paint.Style.FILL
            }

            val margin = 40f
            val rowHeight = 28f
            val tableTopFirstPage = 160f
            val tableTopOtherPages = 60f

            // Column layout
            val colX = floatArrayOf(margin, margin + 155f, margin + 235f, margin + 320f, margin + 410f)
            val colHeaders = arrayOf("Student Name", "CA Score", "Test Score", "Total Score", "Grade")
            val tableWidth = pageWidth - 2 * margin

            // Calculate pagination
            val studentsPerFirstPage = ((pageHeight - tableTopFirstPage - 80) / rowHeight).toInt()
            val studentsPerOtherPage = ((pageHeight - tableTopOtherPages - 80) / rowHeight).toInt()
            val totalPages = if (students.isEmpty()) 1
            else {
                val afterFirst = (students.size - studentsPerFirstPage).coerceAtLeast(0)
                1 + if (afterFirst > 0) ((afterFirst + studentsPerOtherPage - 1) / studentsPerOtherPage) else 0
            }

            var studentIndex = 0

            for (page in 1..totalPages) {
                val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, page).create()
                val pdfPage = document.startPage(pageInfo)
                val canvas: Canvas = pdfPage.canvas
                var yPosition: Float

                val isFirstPage = page == 1
                val tableTop = if (isFirstPage) tableTopFirstPage else tableTopOtherPages
                val maxStudentsThisPage = if (isFirstPage) studentsPerFirstPage else studentsPerOtherPage

                // ── Title & Summary (first page only) ──
                if (isFirstPage) {
                    yPosition = margin

                    // Title
                    canvas.drawText("Student Grade Report", margin, yPosition + 24f, titlePaint)
                    yPosition += 45f

                    // Subtitle
                    val subtitlePaint = Paint(bodyPaint).apply { textSize = 11f; color = Color.GRAY }
                    canvas.drawText(
                        "Total Students: ${students.size}  |  Page $page of $totalPages",
                        margin, yPosition + 12f, subtitlePaint
                    )
                    yPosition += 25f

                    // Summary statistics (using lambdas on student list)
                    if (students.isNotEmpty()) {
                        val passingCount = students.count { it.isPassing }
                        val failingCount = students.size - passingCount
                        val avgScore = students.map { it.totalScore }.average()

                        val statsPaint = Paint(bodyPaint).apply { textSize = 11f }
                        canvas.drawText(
                            "Passing: $passingCount  |  Failing: $failingCount  |  Average Score: ${"%.2f".format(avgScore)}",
                            margin, yPosition + 12f, statsPaint
                        )
                    }
                }

                yPosition = tableTop

                // ── Table Header ──
                canvas.drawRect(margin, yPosition, margin + tableWidth, yPosition + rowHeight, headerBgPaint)
                colHeaders.forEachIndexed { i, header ->
                    canvas.drawText(header, colX[i] + 5f, yPosition + 19f, headerPaint)
                }
                yPosition += rowHeight

                // ── Table Data Rows ──
                val endIndex = minOf(studentIndex + maxStudentsThisPage, students.size)
                for (i in studentIndex until endIndex) {
                    val student = students[i]

                    // Alternating row background
                    if ((i - studentIndex) % 2 == 0) {
                        canvas.drawRect(margin, yPosition, margin + tableWidth, yPosition + rowHeight, altRowPaint)
                    }

                    // Row border
                    canvas.drawRect(margin, yPosition, margin + tableWidth, yPosition + rowHeight, linePaint)

                    // Cell values
                    val truncatedName = if (student.studentName.length > 22)
                        student.studentName.take(22) + "..." else student.studentName
                    canvas.drawText(truncatedName, colX[0] + 5f, yPosition + 19f, bodyPaint)
                    canvas.drawText("%.1f".format(student.caScore), colX[1] + 5f, yPosition + 19f, bodyPaint)
                    canvas.drawText("%.1f".format(student.testScore), colX[2] + 5f, yPosition + 19f, bodyPaint)
                    canvas.drawText("%.1f".format(student.totalScore), colX[3] + 5f, yPosition + 19f, bodyPaint)

                    // Grade column with color (using gradeColorMapper lambda)
                    val gradePaint = Paint(bodyPaint).apply {
                        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                        color = gradeColorMapper(student.grade)
                    }
                    canvas.drawText(student.grade, colX[4] + 5f, yPosition + 19f, gradePaint)

                    yPosition += rowHeight
                }

                studentIndex = endIndex

                // ── Footer ──
                val footerPaint = Paint().apply {
                    color = Color.GRAY
                    textSize = 9f
                    isAntiAlias = true
                }
                canvas.drawText(
                    "Generated by Grade Calculator App  |  Page $page of $totalPages",
                    margin, (pageHeight - 20).toFloat(), footerPaint
                )

                document.finishPage(pdfPage)
            }

            // Write PDF to the output URI
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                document.writeTo(outputStream)
            }
            document.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
