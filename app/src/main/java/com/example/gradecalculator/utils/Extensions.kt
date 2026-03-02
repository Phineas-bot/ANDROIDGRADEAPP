package com.example.gradecalculator.utils

/**
 * Extension functions for commonly used operations
 */

/**
 * Safely parse string to double, returns null if parsing fails
 */
fun String.toDoubleOrNull(): Double? {
    return try {
        this.toDouble()
    } catch (e: NumberFormatException) {
        null
    }
}

/**
 * Format double to 2 decimal places
 */
fun Double.toFormattedString(decimals: Int = 2): String {
    return String.format("%.${decimals}f", this)
}

/**
 * Check if string is a valid number
 */
fun String.isValidNumber(): Boolean {
    return try {
        this.toDouble()
        true
    } catch (e: NumberFormatException) {
        false
    }
}

/**
 * Clamp a double value between min and max
 */
fun Double.clamp(min: Double, max: Double): Double {
    return when {
        this < min -> min
        this > max -> max
        else -> this
    }
}

/**
 * Get ordinal suffix for numbers (1st, 2nd, 3rd, 4th, etc)
 */
fun Int.toOrdinal(): String {
    return when {
        this % 100 in 11..13 -> "${this}th"
        this % 10 == 1 -> "${this}st"
        this % 10 == 2 -> "${this}nd"
        this % 10 == 3 -> "${this}rd"
        else -> "${this}th"
    }
}

/**
 * Check if a list is empty with safe navigation
 */
fun <T> List<T>?.isEmptyOrNull(): Boolean {
    return this == null || this.isEmpty()
}

/**
 * Get single element or null from list
 */
fun <T> List<T>?.getOrNull(index: Int): T? {
    return if (this != null && index in indices) this[index] else null
}

