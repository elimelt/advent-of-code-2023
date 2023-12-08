import java.io.File
const val INPUT_FILE = "./input.txt"


class IntervalMap {
    private val intervalSrc = mutableListOf<Pair<Long, Long>>()
    private val intervalDest = mutableListOf<Pair<Long, Long>>()

    fun addInterval(destStart: Long, srcStart: Long, len: Long) {
        intervalSrc.add(Pair(srcStart, srcStart + len))
        intervalDest.add(Pair(destStart, srcStart + len))
    }

    fun getMappedVal(n: Long): Long{
        for ((i, interval) in intervalSrc.withIndex()) {
            if (n >= interval.first && n <= interval.second)
                return intervalDest[i].first + (n - interval.first)
        }

        return n
    }

    fun sortBySrcIntervalStart() {
        val zipped = intervalSrc.zip(intervalDest)
        val sorted = zipped.sortedBy { it.first.first }
        intervalSrc.clear()
        intervalDest.clear()
        for (pair in sorted) {
            intervalSrc.add(pair.first)
            intervalDest.add(pair.second)
        }
    }

    fun getIntervalsOverlapping(start: Long, end: Long): MutableList<Pair<Long, Long>> {
        val intervals = mutableListOf<Pair<Long, Long>>()
        for ((i, interval) in intervalSrc.withIndex()) {
            if (interval.first <= end && interval.second >= start)
                intervals.add(intervalDest[i])
        }

        return intervals
    }


}

fun readAlmanac(input: List<String>): List<IntervalMap> {
    val intervalMaps = mutableListOf<IntervalMap>()

    for (line in input) {
        if (line.contains("map:")) {
            intervalMaps.add(IntervalMap())
            continue
        } else if (line.length > 0 && line[0].isDigit()) {
            val (destStart, srcStart, len) = line.split(" ").map { it.toLong() }
            intervalMaps.last().addInterval(destStart, srcStart, len)
        }
    }


    return intervalMaps
}

fun findSeedLocations(input: List<String>): List<Long> {
    val seeds: List<Long> = input[0].split(":")[1].trim().split(" ").map { it.toLong() }
    val almanac = readAlmanac(input)

    val seedLocations = mutableListOf<Long>()

    for (seed in seeds) {
        var curr: Long = seed
        for (intervalMap in almanac)
            curr = intervalMap.getMappedVal(curr)

        seedLocations.add(curr)
    }

    return seedLocations
}

// fun findSeedLocationFromRanges(input: List<String>): Long {
//     val seeds: List<Long> = input[0].split(":")[1].trim().split(" ").map { it.toLong() }
//     val almanac = readAlmanac(input)

//     almanac.forEach { it.sortBySrcIntervalStart() }

// }

fun mergeIntervals(intervals: List<Pair<Long, Long>>): List<Pair<Long, Long>> {
    val sorted = intervals.sortedBy { it.first }
    val merged = mutableListOf<Pair<Long, Long>>()
    var curr = sorted[0]

    for (interval in sorted) {
        if (interval.first <= curr.second) {
            curr = Pair(curr.first, interval.second)
        } else {
            merged.add(curr)
            curr = interval
        }
    }

    merged.add(curr)

    return merged
}

fun splitIntervals(interval: Pair<Long, Long>, sortedCollapsedIntervals: MutableList<Long>): List<Pair<Long, Long>> {
    var out = mutableListOf<Pair<Long, Long>>()
    sortedCollapsedIntervals.add(interval.first)
    sortedCollapsedIntervals.add(interval.second)

    var visited = mutableSetOf<Long>()

    for ((i, n) in sortedCollapsedIntervals.withIndex()) {
        if (n == interval.first || visited.contains(n))
            continue

        if (i == 0)
            continue
        if (n >= interval.first && n <= interval.second)
            out.add(Pair(Math.max(interval.first, sortedCollapsedIntervals[i - 1]), n))

        visited.add(n)
    }

    return out
}

fun transition(almanac: List<IntervalMap>, intervals: List<Pair<Long, Long>>, i: Int): List<Pair<Long, Long>> {
    var out = mutableListOf<Pair<Long, Long>>()

    map: IntervalMap = almanac[i]

    for (interval in intervals) {
        val overlapping = almanac[0].getIntervalsOverlapping(interval.first, interval.second)
        for (overlap in overlapping) {
            out.add(Pair(overlap.first, overlap.second))
        }
    }

    return out
}

fun collapseIntervalsIntoSortedList(intervals: List<Pair<Long, Long>>): List<Long> {
    var out = mutableListOf<Long>()
    for (interval in intervals) {
        out.add(interval.first)
        out.add(interval.second)
    }
    return out.sorted()
}


// fun recursiveSearchForMin(almanac: List<IntervalMap>, intervals: List<Pair<Long, Long>>, i: Int): Long {
//     IntervalMap iMap = almanac[i]
//     var nextIntervals = mutableListOf<Pair<Long, Long>>()

//     for ((i, interval) in intervals.withIndex()) {
//         val overlapping = iMap.getIntervalsOverlapping(interval.first, interval.second)

//     }

// }

fun main() {
    // dest_start, src_start, len
    val input = File(INPUT_FILE).readLines()

    // println(findSeedLocations(input).min())

    println(splitIntervals(Pair(0, 10), mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14)))


}