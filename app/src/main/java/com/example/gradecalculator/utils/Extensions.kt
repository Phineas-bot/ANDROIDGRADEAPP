package com.example.gradecalculator.utils

/**
 * Extension functions for commonly used operations.
 * Demonstrates Kotlin extension functions (a functional programming feature).
 */

/**
 * Formats a Double to a string with the specified number of decimal places.
 */
fun Double.toFormattedString(decimals: Int = 2): String {
    return String.format("%.${decimals}f", this)
}

/**
 * Checks if a string represents a valid numeric value.
 */
fun String.isValidNumber(): Boolean {
    return this.toDoubleOrNull() != null
}

/**
 * Clamps a Double value between min and max bounds.
 */
fun Double.clamp(min: Double, max: Double): Double {
    return when {
        this < min -> min
        this > max -> max
        else -> this
    }
}

/**
 * Gets ordinal suffix for numbers (1st, 2nd, 3rd, 4th, etc).
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
 * Checks if a list is null or empty with safe navigation.
 */
fun <T> List<T>?.isEmptyOrNull(): Boolean {
    return this == null || this.isEmpty()
}
