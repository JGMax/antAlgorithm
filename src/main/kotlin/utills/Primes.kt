package utills

import node.NodesBuffer

object Primes {
    val primes: Array<Int> by lazy {
        calcPrimes(NodesBuffer.size)
    }
}