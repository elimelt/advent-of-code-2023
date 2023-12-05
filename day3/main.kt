import java.io.File
const val INPUT_FILE = "./input.txt"

fun isSymbol(c: Char): Boolean = c != '.' && !c.isDigit()

fun checkSurroundings(i: Int, j: Int, input: List<String>): Boolean {
    val left = if (j == 0) '.' else input[i][j - 1]
    val upLeft = if (i == 0 || j == 0) '.' else input[i - 1][j - 1]
    val downLeft = if (i == input.size - 1 || j == 0) '.' else input[i + 1][j - 1]
    if (isSymbol(left) || isSymbol(upLeft) || isSymbol(downLeft))
        return true

    var k = j
    while (k < input[i].length && input[i][k].isDigit()) {
        val up = if (i == 0) '.' else input[i - 1][k]
        val down = if (i == input.size - 1) '.' else input[i + 1][k]
        if (isSymbol(up) || isSymbol(down))
            return true
        k++
    }

    val upRight = if (i == 0 || k >= input[i].length - 1) '.' else input[i - 1][k]
    val downRight = if (i == input.size - 1 || k >= input[i].length - 1) '.' else input[i + 1][k]
    val right = if (k == input[i].length) '.' else input[i][k]
    return isSymbol(upRight) || isSymbol(downRight) || isSymbol(right)
}

fun sumPartNumbers(input: List<String>): Int {
    var c: Char
    var i = 0
    var j = 0

    var ans = 0
    while (i < input.size) {
        while (j < input[i].length) {
            c = input[i][j]
            if (c.isDigit() && checkSurroundings(i, j, input)) {

                var numstr = ""
                while (j < input[i].length && input[i][j].isDigit())
                    numstr += input[i][j++]

                val num = numstr.toInt()
                ans += num
            }
            j++
        }
        j = 0
        i++
    }
    return ans
}

fun getNumAt(i: Int, j: Int, input: List<String>, visited: MutableSet<Pair<Int, Int>>): Int {
    var k = j
    var numstr = ""
    while (k >= 0 && input[i][k].isDigit())
        k--;
    k++;
    while (k < input[i].length && input[i][k].isDigit()) {
        visited.add(Pair(i, k))
        numstr += input[i][k++]
    }
    return numstr.toInt()
}

fun getNumsBy(i: Int, j: Int, input: List<String>): List<Int> {
    var nums = mutableListOf<Int>()
    var visited = mutableSetOf<Pair<Int, Int>>()

    var adj = mutableListOf<Pair<Int, Int>>(
        Pair(i, j - 1),
        Pair(i - 1, j - 1),
        Pair(i + 1, j - 1),
        Pair(i - 1, j),
        Pair(i + 1, j),
        Pair(i, j + 1),
        Pair(i - 1, j + 1),
        Pair(i + 1, j + 1)
    )

    for (p in adj) {
        if (p.first >= 0 && p.first < input.size && p.second >= 0 && p.second < input[p.first].length) {
            if (input[p.first][p.second].isDigit() && !visited.contains(p)) {
                nums.add(getNumAt(p.first, p.second, input, visited))
            }
        }
    }

    return nums
}

fun sumGearRatios(input: List<String>): Int {
    var sum = 0
    for (i in 0 until input.size) {
        for (j in 0 until input[i].length) {
            if (input[i][j] == '*') {
                val nums = getNumsBy(i, j, input)
                if (nums.size == 2)
                    sum += nums[0] * nums[1]
            }
        }
    }
    return sum
}

fun main() {
    val input = File(INPUT_FILE).readLines()

    // part one
    println(sumPartNumbers(input))

    // part two
    println(sumGearRatios(input))

}