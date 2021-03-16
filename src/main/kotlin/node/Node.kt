package node

import kotlin.math.pow
import kotlin.math.sqrt

data class Node(val id: Int, val x: Double, val y: Double) {

    fun calcDistance(node: Node) =
        sqrt((x - node.x).pow(2) + (y - node.y).pow(2))
}
