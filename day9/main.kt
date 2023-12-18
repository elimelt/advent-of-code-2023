import java.io.File
val INPUT_FILE = "./input.txt"

fun readInput(input: List<String>): List<List<Int>> =
    input.map { it.split(" ").map{ it.toInt() } }

fun calculateDifferences(sequence: List<Int>): List<Int> {
    val differences = mutableListOf<Int>()
    for (i in 1 until sequence.size)
        differences.add(sequence[i] - sequence[i - 1])

    return differences
}

fun extrapolateForward(sequence: List<Int>): Int {
    val prev = sequence.toMutableList()
    val lastNumbers = mutableListOf<Int>(prev.last())
    for (i in 0 until sequence.size) {
        val differences = calculateDifferences(prev)

        if (differences.all { it == 0 })
            break

        lastNumbers.add(differences.last())
        prev.clear()
        prev.addAll(differences)
    }

    return lastNumbers.sum()
}

fun extrapolateBackwards(sequence: List<Int>): Int {
    val prev = sequence.toMutableList()
    val firstNumbers = mutableListOf<Int>(prev.first())
    for (i in 0 until sequence.size) {
        val differences = calculateDifferences(prev)

        if (differences.all { it == 0 })
            break

        firstNumbers.add(differences.first())
        prev.clear()
        prev.addAll(differences)
    }

    var curr = 0

    for (n in firstNumbers.reversed()) {
        curr = n - curr
    }

    return curr
}

fun main() {
    val input = File(INPUT_FILE).readLines()

    // part 1
    println(readInput(input).map { extrapolateForward(it) }.sum())

    // part 2
    println(readInput(input).map { extrapolateBackwards(it) }.sum())
}