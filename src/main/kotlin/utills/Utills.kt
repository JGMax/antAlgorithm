package utills
import settings.Constants.GLOBAL_MIN_LINE_COLOR
import settings.Constants.LOCAL_MIN_LINE_COLOR
import node.Node
import settings.Constants
import utills.Primes
import visualization.Chart
import kotlin.random.Random

fun roulette(probabilities: Array<Double>) : Int {
    var num = Random.nextDouble(1.0)
    probabilities.forEachIndexed { i, probability ->
        num -= probability
        if (num <= 0) {
            return i
        }
    }
    return -1
}

fun calcPrimes(until: Int) : Array<Int> {
    val primes: MutableList<Int> = MutableList(0) { 1 }
    for (i in 2 until until) {
        var isPrime = true
        for (prime in primes) {
            if (i % prime == 0) {
                isPrime = false
                break
            }
        }
        if (isPrime) {
            primes.add(i)
        }
    }
    return primes.toTypedArray()
}

fun objectsWake() {
    Chart.addSeries(
        Constants.GLOBAL_MIN_LINE_NAME
        , arrayOf(MutableList(1) { 0.0 }
            , MutableList(1) { 0.0 })
        , GLOBAL_MIN_LINE_COLOR
        , 2.5f)

    Chart.addSeries(
        Constants.LOCAL_MIN_LINE_NAME
        , arrayOf(MutableList(1) { 0.0 }
            , MutableList(1) { 0.0 })
        , LOCAL_MIN_LINE_COLOR)

    Primes.primes
}
