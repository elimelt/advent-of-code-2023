import java.io.File


const val FILENAME = "./input.txt"


fun getInstructionOrder(input: List<String>): List<Int> =
    input[0].toCharArray().map{ when (it) {
        'L' -> 0
        'R' -> 1
        else -> -1
    } }

fun readNetwork(input: List<String>): Map<String, Pair<String, String>> {
    val network = mutableMapOf<String, Pair<String, String>>()
    val pattern = Regex("""(\w+)\s*=\s*\(([^,]+),\s*([^)]+)\)""")

    for ((i, line) in input.withIndex()) {
        if (i == 0 || line.isEmpty())
            continue

        pattern.find(line)?.let {
            val (key, left, right) = it.destructured
            network[key] = Pair(left, right)
        }
    }
    return network
}

fun traverseNetwork(
    network: Map<String, Pair<String, String>>,
    moveSet: List<Int>,
    startNode: String,
    testFunction: (String) -> Boolean = { it == "ZZZ" },
): Int {

    var i = 0
    var currentNode = startNode
    while (true) {
        if (testFunction(currentNode))
            return i

        val move = moveSet[i % moveSet.size]

        if (move == 0)
            currentNode = network[currentNode]!!.first
        else if (move == 1)
            currentNode = network[currentNode]!!.second

        i++
    }
}

fun lcm(a: Long, b: Long): Long {
    var ans = a
    while (ans % b != 0L)
        ans += a
    return ans
}

fun lcmOfArray(arr: List<Int>): Long {
    var ans = arr[0].toLong()
    for (i in 1 until arr.size) {
        ans = lcm(ans, arr[i].toLong())
    }
    return ans
}

fun multiTraverseNetwork(
    network: Map<String, Pair<String, String>>,
    moves: List<Int>): Long {

    fun isEndNode(node: String): Boolean = node.endsWith("Z")
    val bag = network.keys.filter({ it.endsWith("A") }).toMutableList()
    val minMoves = bag.map { traverseNetwork(
        network, moves, it, ::isEndNode
    )}

    val ans = lcmOfArray(minMoves)

    return ans
}

fun main() {
    val input = File(FILENAME).readLines()

    val ins = getInstructionOrder(input)
    val net = readNetwork(input)

    // part 1
    println(traverseNetwork(net, ins, "AAA"))

    // part 2
    println(multiTraverseNetwork(net, ins))
}