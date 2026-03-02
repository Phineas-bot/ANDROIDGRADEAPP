package com.example.gradecalculator.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gradecalculator.model.Course
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) interface for Course database operations
 * Handles all database queries and mutations
 */
@Dao
interface CourseDao {

    /**
     * Inserts a new course into the database
     * @param course The course to insert
     * @return Row ID of the inserted course
     */
    @Insert
    suspend fun insertCourse(course: Course): Long

    /**
     * Updates an existing course
     * @param course The course to update
     */
    @Update
    suspend fun updateCourse(course: Course)

    /**
     * Deletes a course
     * @param course The course to delete
     */
    @Delete
    suspend fun deleteCourse(course: Course)

    /**
     * Retrieves all courses as a Flow for reactive updates
     * @return Flow of list of all courses
     */
    @Query("SELECT * FROM courses ORDER BY timestamp DESC")
    fun getAllCourses(): Flow<List<Course>>

    /**
     * Retrieves all courses by student name
     * @param studentName The student name to filter by
     * @return Flow of list of courses for the student
     */
    @Query("SELECT * FROM courses WHERE studentName = :studentName ORDER BY timestamp DESC")
    fun getCoursesByStudent(studentName: String): Flow<List<Course>>

    /**
     * Retrieves a specific course by ID
     * @param courseId The ID of the course
     * @return Flow of the course or null if not found
     */
    @Query("SELECT * FROM courses WHERE id = :courseId")
    fun getCourseById(courseId: String): Flow<Course?>

    /**
     * Gets count of all courses
     * @return Flow of course count
     */
    @Query("SELECT COUNT(*) FROM courses")
    fun getCourseCount(): Flow<Int>

    /**
     * Deletes all courses (for reset functionality)
     */
    @Query("DELETE FROM courses")
    suspend fun deleteAllCourses()

    /**
     * Gets distinct student names from database
     * @return Flow of list of unique student names
     */
    @Query("SELECT DISTINCT studentName FROM courses ORDER BY studentName")
    fun getDistinctStudentNames(): Flow<List<String>>
}

