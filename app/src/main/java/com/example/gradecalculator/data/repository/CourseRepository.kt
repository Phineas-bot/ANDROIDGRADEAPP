package com.example.gradecalculator.data.repository

import com.example.gradecalculator.data.local.CourseDao
import com.example.gradecalculator.model.Course
import kotlinx.coroutines.flow.Flow

/**
 * Repository class for managing course data
 * Acts as an abstraction layer between ViewModel and data sources
 */
class CourseRepository(private val courseDao: CourseDao) {

    /**
     * Inserts a new course
     * @param course The course to insert
     */
    suspend fun insertCourse(course: Course) {
        courseDao.insertCourse(course)
    }

    /**
     * Updates an existing course
     * @param course The course to update
     */
    suspend fun updateCourse(course: Course) {
        courseDao.updateCourse(course)
    }

    /**
     * Deletes a course
     * @param course The course to delete
     */
    suspend fun deleteCourse(course: Course) {
        courseDao.deleteCourse(course)
    }

    /**
     * Gets all courses as a Flow
     * @return Flow of list of courses
     */
    fun getAllCourses(): Flow<List<Course>> {
        return courseDao.getAllCourses()
    }

    /**
     * Gets courses by student name
     * @param studentName The student name to filter by
     * @return Flow of list of courses
     */
    fun getCoursesByStudent(studentName: String): Flow<List<Course>> {
        return courseDao.getCoursesByStudent(studentName)
    }

    /**
     * Gets a specific course by ID
     * @param courseId The course ID
     * @return Flow of the course
     */
    fun getCourseById(courseId: String): Flow<Course?> {
        return courseDao.getCourseById(courseId)
    }

    /**
     * Gets the count of all courses
     * @return Flow of course count
     */
    fun getCourseCount(): Flow<Int> {
        return courseDao.getCourseCount()
    }

    /**
     * Deletes all courses
     */
    suspend fun deleteAllCourses() {
        courseDao.deleteAllCourses()
    }

    /**
     * Gets distinct student names
     * @return Flow of list of student names
     */
    fun getDistinctStudentNames(): Flow<List<String>> {
        return courseDao.getDistinctStudentNames()
    }
}

