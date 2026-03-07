package com.example.gradecalculator.homework

fun processList(
    numbers: List<Int>,
    predicate: (Int) -> Boolean
): List<Int> {
    return numbers.filter(predicate)
}

fun printWordsLongerThanFour() {
    val words = listOf("apple", "cat", "banana", "dog", "elephant")

    val lengthMap: Map<String, Int> = words.associateWith { it.length }

    lengthMap
        .filter { it.value > 4 }
        .forEach { (word, length) ->
            println("$word has length $length")
        }
}

data class Person(val name: String, val age: Int)

fun Double.formatToOneDecimal(): String = "%.1f".format(this)

fun printAverageAgeForNamesStartingWithAOrB() {
    val people = listOf(
        Person("Alice", 25),
        Person("Bob", 30),
        Person("Charlie", 35),
        Person("Anna", 22),
        Person("Ben", 28)
    )

    val matchingAges = people
        .filter { it.name.startsWith("A") || it.name.startsWith("B") }
        .map { it.age }

    val average = if (matchingAges.isNotEmpty()) {
        matchingAges.average()
    } else {
        0.0
    }

    println("Average age: ${average.formatToOneDecimal()}")
}

fun main() {
    println("Task 1:")
    val nums = listOf(1, 2, 3, 4, 5, 6)
    val even = processList(nums) { it % 2 == 0 }
    println(even)

    println()

    println("Task 2:")
    printWordsLongerThanFour()

    println()

    println("Task 3:")
    printAverageAgeForNamesStartingWithAOrB()
}
