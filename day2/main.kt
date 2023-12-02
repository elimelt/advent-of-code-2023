import java.io.File

const val INPUT_FILE = "input.txt"

data class Game(val id: Int, val blue: List<Int>, val green: List<Int>, val red: List<Int>) {
    fun addRound(roundBlue: Int, roundGreen: Int, roundRed: Int): Game {
        return copy(
            blue = blue + roundBlue,
            green = green + roundGreen,
            red = red + roundRed
        )
    }
}

fun parseLine(line: String, earlyReturn: Boolean = true): Game? {
    val parts = line.split(": ")
    val id = parts[0].split(" ")[1].toInt()
    val rounds = parts[1].split("; ")

    var game = Game(id, emptyList(), emptyList(), emptyList())

    for (round in rounds) {
        val roundParts = round.split(", ")

        var roundBlue = 0
        var roundGreen = 0
        var roundRed = 0

        for (part in roundParts) {
            val (count, color) = part.split(" ")
            val countValue = count.toInt()

            when (color) {
                "blue" -> {
                    if (earlyReturn && countValue > 14) return null
                    roundBlue = countValue
                }
                "green" -> {
                    if (earlyReturn && countValue > 13) return null
                    roundGreen = countValue
                }
                "red" -> {
                    if (earlyReturn && countValue > 12) return null
                    roundRed = countValue
                }
            }
        }

        game = game.addRound(roundBlue, roundGreen, roundRed)
    }

    return game
}

fun countId(game: Game?): Int = game?.id ?: 0

fun countPower(game: Game): Int = game.blue.maxOrNull()!! * game.green.maxOrNull()!! * game.red.maxOrNull()!!


fun main() {
    // Part 1
    val ans1 = File(INPUT_FILE).readLines().sumOf { line -> countId(parseLine(line)) }
    println("Part one: $ans1")

    // Part 2
    val ans2 = File(INPUT_FILE).readLines().sumOf { line -> countPower(parseLine(line, earlyReturn = false)!!) }
    println("Part two: $ans2")
}
