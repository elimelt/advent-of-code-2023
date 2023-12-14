import java.io.File
const val FILENAME = "input.txt"

// IMPORTANT NOTE
// -----------------------------
// I made all functions one-liners for fun. This is NOT
// production code. I would never do this in production.

fun readInputPart1(input: List<String>): List<Pair<Long, Long>> =
    input[0]
        .split(":")[1]
        .trim()
        .split("\\s+".toRegex())
        .map { it.toLong() }
    .zip(input[1]
        .split(":")[1]
        .trim()
        .split("\\s+".toRegex())
        .map { it.toLong() })


fun readInputPart2(input: List<String>): Pair<Long, Long> = Pair(
    input[0]
        .split(":")[1]
        .trim()
        .split("\\s+".toRegex())
        .joinToString("")
        .toLong(),
    input[1]
        .split(":")[1]
        .trim()
        .split("\\s+".toRegex())
        .joinToString("")
        .toLong()
)


class Race(val time: Long, val recordDistance: Long) {

    constructor(pair: Pair<Long, Long>) : this(pair.first, pair.second)

    fun holdButtonFor(t: Long): Long = ((time - t) * t)

    fun findNumberOfViableTimes(): Long =
        generateSequence(time) { it - 1 }
            .takeWhile { holdButtonFor(it) <= recordDistance }
            .last() -
        generateSequence(0L) { it + 1 }
            .takeWhile { holdButtonFor(it) <= recordDistance }
            .last() - 1
}

fun getProductOfViableCounts(input: List<String>): Long =
    readInputPart1(input)
        .fold(1L) { acc, (time, dist) -> acc * Race(time, dist)
            .findNumberOfViableTimes()
        }

fun getViableCountForOneLongRace(input: List<String>): Long =
    Race(readInputPart2(input)).findNumberOfViableTimes();


fun main() {
    val input = File(FILENAME).readLines()

    // part 1
    println(getProductOfViableCounts(input))

    // part 2
    println(getViableCountForOneLongRace(input))
}