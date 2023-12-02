package day1
import java.io.File

const val INPUT_FILE = "input.txt"

val digitStrings = setOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
val digitVals = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9)

fun getCalibrationNumberPartOne(line: String): Int =
    (line[line.indexOfFirst(Char::isDigit)].toString() + line[line.indexOfLast(Char::isDigit)].toString()).toInt()


fun getSemiDigitStartingFrom(s: String, i: Int): Int? =
    if (s[i].isDigit())
        s[i].toString().toInt()
    else
        (2 until 6).mapNotNull { j ->
            if (i + j <= s.length && s.substring(i, i + j) in digitStrings)
                digitVals[s.substring(i, i + j)]
            else
                null
    }.firstOrNull()

fun getFirstSemiDigit(s: String): Int = generateSequence(0) { it + 1 }
    .mapNotNull { getSemiDigitStartingFrom(s, it) }
    .first()

fun getLastSemiDigit(s: String): Int = generateSequence(s.length - 1) { it - 1 }
    .mapNotNull { getSemiDigitStartingFrom(s, it) }
    .first()

fun getCalibrationNumberPartTwo(line: String): Int = (getFirstSemiDigit(line).toString() + getLastSemiDigit(line).toString()).toInt()


/*
Let N be the number of lines in the input file, and let M be the length of the longest line.
*/
fun main() {

    // Part 1 solution. Running time: O(NM)
    // one line solution:  val ans = File(INPUT_FILE).readLines().sumOf { (it[it.indexOfFirst { it.isDigit() }].toString() + it[it.indexOfLast { it.isDigit() }].toString()).toInt() }
    val partOneSum = File(INPUT_FILE).readLines().sumOf { getCalibrationNumberPartOne(it) }
    println("Part one: $partOneSum")

    // Part 2 solution. Running time: O(NM)
    val partTwoSum = File(INPUT_FILE).readLines().sumOf { getCalibrationNumberPartTwo(it) }
    println("Part two: $partTwoSum")
}
