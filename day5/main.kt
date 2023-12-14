import java.io.File
const val INPUT_FILE = "./input.txt"

fun readInput(input: List<String>): Triple<List<IntervalMap>, List<Long>, List<Pair<Long, Long>>> {
    val almanac = mutableListOf<IntervalMap>()
    val individualSeeds = input[0].split(":")[1].trim().split(" ").map { it.toLong() }
    val seedRanges = input[0].split(":")[1].trim().split(" ").map { it.toLong() }

    for (line in input) {
        if (line.contains("map:"))
            almanac.add(IntervalMap())
        else if (line.length > 0 && line[0].isDigit()) {
            val (destStart, srcStart, len) = line.split(" ").map { it.toLong() }
            almanac.last().addInterval(srcStart, destStart, len)
        }
    }

    val seedIntervals = mutableListOf<Pair<Long, Long>>()
    for (i in 0 until seedRanges.size step 2)
        seedIntervals.add(Pair(seedRanges[i], seedRanges[i + 1] + seedRanges[i]))

    return Triple(almanac, individualSeeds, seedIntervals)
}


class IntervalMap {
    private val intervalSrc = mutableListOf<Pair<Long, Long>>()
    private val intervalDest = mutableListOf<Pair<Long, Long>>()

    fun addInterval(srcStart: Long, destStart: Long, len: Long) {
        intervalSrc.add(Pair(srcStart, srcStart + len))
        intervalDest.add(Pair(destStart, srcStart + len))
    }

    fun getMappedVal(n: Long, inclStart: Boolean = true, inclEnd: Boolean = true): Long {
        for ((i, interval) in intervalSrc.withIndex())
            if ((n > interval.first && n < interval.second) ||
                (inclStart && n == interval.first) ||
                (inclEnd && n == interval.second))
                return intervalDest[i].first + (n - interval.first)

        return n
    }

    fun getIntervalsOverlapping(start: Long, end: Long): MutableList<Pair<Long, Long>> {
        val intervals = mutableListOf<Pair<Long, Long>>()
        for ((i, interval) in intervalSrc.withIndex()) {
            if (interval.first <= end && interval.second >= start)
                intervals.add(intervalSrc[i])
        }

        return intervals
    }
}

fun findSeedLocationsOneByOne(almanac: List<IntervalMap>, seeds: List<Long>): List<Long> {

    val seedLocations = mutableListOf<Long>()

    for (seed in seeds) {
        var curr: Long = seed
        for (intervalMap in almanac)
            curr = intervalMap.getMappedVal(curr)

        seedLocations.add(curr)
    }

    return seedLocations
}

fun splitIntervals(interval: Pair<Long, Long>, breakPoints: Set<Long>): List<Pair<Long, Long>> {
    var out = mutableListOf<Pair<Long, Long>>()

    var visited = mutableSetOf<Long>()

    var curr = interval.first
    for (breakPoint in breakPoints.sorted()) {
        if (breakPoint > interval.second)
            break

        if (breakPoint > curr) {
            out.add(Pair(curr, breakPoint))
            curr = breakPoint
        }

        visited.add(breakPoint)
    }

    if (curr < interval.second)
        out.add(Pair(curr, interval.second))

    return out
}

fun collapseIntervalsIntoList(intervals: List<Pair<Long, Long>>): MutableList<Long> {
    var out = mutableListOf<Long>()
    for (interval in intervals) {
        out.add(interval.first)
        out.add(interval.second)
    }
    return out
}

fun mapIntervalsToNextLayer(iMap: IntervalMap, intervals: List<Pair<Long, Long>>): List<Pair<Long, Long>> {
    var breakPoints = mutableSetOf<Long>()
    var out = mutableListOf<Pair<Long, Long>>()

    intervals.forEach {
        breakPoints.add(it.first)
        breakPoints.add(it.second)

        iMap.getIntervalsOverlapping(it.first, it.second).forEach { sourceRange ->
            if (sourceRange.first > it.first)
                breakPoints.add(sourceRange.first)
            if (sourceRange.second < it.second)
                breakPoints.add(sourceRange.second)
        }
    }

    intervals.forEach { out.addAll(splitIntervals(it, breakPoints)) }

    return out.map {
        Pair(iMap.getMappedVal(it.first, true, false),
             iMap.getMappedVal(it.second, false, true))
    }

}


fun searchAllLayers(almanac: List<IntervalMap>, intervals: List<Pair<Long, Long>>, i: Int): Long =
    if (i == almanac.size)
        intervals.minBy { it.first }.first
    else
        searchAllLayers(almanac, mapIntervalsToNextLayer(almanac[i], intervals), i + 1)


fun findMinSeedLocation(almanac: List<IntervalMap>, inputIntervals: List<Pair<Long, Long>>): Long = searchAllLayers(almanac, inputIntervals, 0);


fun main() {
    val input = File(INPUT_FILE).readLines()

    val (almanac, individualSeeds, seedIntervals) = readInput(input)

    // part 1
    println(findSeedLocationsOneByOne(almanac, individualSeeds).min())

    // part 2 6472060
    println(findMinSeedLocation(almanac, seedIntervals))

}