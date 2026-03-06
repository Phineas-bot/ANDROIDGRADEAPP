package com.example.gradecalculator.interfaces

import android.content.Context
import android.net.Uri
import com.example.gradecalculator.model.Student

/**
 * Interface for importing student data from files.
 * Demonstrates OOP interface for file processing abstraction.
 */
interface FileImportable {
    /**
     * Imports student data from the given file URI.
     * @param context Android context for content resolver access
     * @param uri URI of the file to import
     * @return List of imported Student objects
     */
    fun importStudents(context: Context, uri: Uri): List<Student>
}

/**
 * Interface for exporting student data to files.
 * Demonstrates OOP interface for file export abstraction.
 */
interface FileExportable {
    /**
     * Exports student data to the given file URI.
     * @param context Android context for content resolver access
     * @param students List of students to export
     * @param uri URI of the output file
     * @return true if the export was successful, false otherwise
     */
    fun exportStudents(context: Context, students: List<Student>, uri: Uri): Boolean
}
