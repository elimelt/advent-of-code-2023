import java.io.File
const val FILENAME = "input.txt"

fun readInput(input: List<String>, jokerRule: Boolean = false): List<Pair<Hand, Int>> =
    input.map { it.split(" ").let { Pair(Hand(it[0], jokerRule), it[1].toInt()) } }


class Hand(val s: String, val jokerRule: Boolean = false): Comparable<Hand> {
    val cards: List<Card>
    val typeRank: Int = -1
    init { cards = s.toCharArray().map { Card(it, jokerRule) } }

    fun getCounts(): Map<Card, Int> =
        cards.groupingBy { it }.eachCount()

    fun getTypeVal(): Int {
        val counts = getCounts().toMutableMap()

        if (jokerRule && counts.containsKey(Card('J'))) {
            // Temporarily remove the joker from counts for comparisons
            val jokerCount = counts[Card('J')]!!
            counts.remove(Card('J'))

            val maxCountCard = counts.maxByOrNull { it.value }?.key

            if (maxCountCard != null) {
                counts.put(maxCountCard, counts[maxCountCard]!! + jokerCount)
            } else {
                // If all cards are distinct, consider the joker as the card that maximizes hand strength
                counts[Card('J')] = jokerCount
            }
        }

        val values = counts.values
        return when {
            values.contains(5) -> 7 // five of a kind
            values.contains(4) -> 6 // four of a kind
            values.contains(3) && values.contains(2) -> 5 // full house
            values.contains(3) -> 4 // three of a kind
            values.filter { it == 2 }.count() == 2 -> 3 // two pair
            values.contains(2) -> 2 // one pair
            else -> 1 // high card
        }
    }

    override fun compareTo(other: Hand): Int {
        val typeRankComparison = getTypeVal().compareTo(other.getTypeVal())
        if (typeRankComparison != 0)
            return typeRankComparison

        cards.map { it.value }.zip(other.cards.map { it.value }).forEach {
            val cardValueComparison = it.first.compareTo(it.second)
            if (cardValueComparison != 0)
                return cardValueComparison
        }

        return 0
    }

    override fun toString(): String = cards.joinToString("")
}

open class Card(val label: Char, val jokerRule: Boolean = false) {
    open val value: Int
        get() = if (jokerRule) when (label) {
            'T' -> 10
            'Q' -> 11
            'K' -> 12
            'A' -> 13
            'J' -> 1
            else -> Character.getNumericValue(label)
        } else when (label) {
            'T' -> 10
            'J' -> 11
            'Q' -> 12
            'K' -> 13
            'A' -> 14
            else -> Character.getNumericValue(label)
        }

    override fun toString(): String = label.toString()

    override fun equals(other: Any?): Boolean =
        (other is Card) && (this === other || label == other.label)


    override fun hashCode(): Int {
        return label.hashCode()
    }
}


fun getTotalWinnings(hands: List<Pair<Hand, Int>>): Int =
    hands
        .sortedBy { it.first }
        .mapIndexed { i, hand -> (i + 1) * hand.second }
        .sum()

fun main() {
    val input = File(FILENAME).readLines()

    // part 1
    println(getTotalWinnings(readInput(input)))
    // part 2
    println(getTotalWinnings(readInput(input, true)))
}