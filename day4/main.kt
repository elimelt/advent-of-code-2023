import java.io.File
const val INPUT_FILE = "./input.txt"

fun parseCard(card: String): Pair<List<Int>, List<Int>> {
    val (winning, nums) = card
        .split(": ")
        .last()
        .split(" | ")
        .map {
            it.trim().split("\\s+".toRegex()).map { it.toInt() }
        }

    return Pair(winning, nums)
}

fun calculateMatches(card: String): Int {
    var (winning, nums) = parseCard(card)
    return nums.toSet().intersect(winning.toSet()).size
}

fun calculateCardScore(card: String): Int {
    val matches = calculateMatches(card)
    return if (matches == 0) 0 else 1 shl (matches - 1)
}

fun calculateTotalScore(input: List<String>): Int = input.sumOf { calculateCardScore(it) }

fun createScoreTable(input: List<String>): List<Int> = input.map { calculateMatches(it) }

fun calculateNumCards(scores: List<Int>): Int {
    var copies = MutableList<Int>(scores.size) { 1 }

    for ((i, score) in scores.withIndex())
        for (j in i + 1 until score + i + 1)
            copies[j] += copies[i]

    return copies.sum()
}

fun main() {
    val input = File(INPUT_FILE).readLines()

    // part 1
    println("Total score: ${calculateTotalScore(input)}")

    // part 2
    println("Total cards: ${calculateNumCards(createScoreTable(input))}")

}