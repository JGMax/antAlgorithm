package edge

import settings.Constants.ALPHA
import settings.Constants.BETA
import settings.Constants.DEFAULT_PHEROMONE
import settings.Constants.DISAPPEARANCE
import settings.Constants.MAX_PHEROMONE
import settings.Constants.MIN_PHEROMONE
import node.Node
import kotlin.math.pow

data class Edge(val node1: Node, val node2: Node) {
    var unpoweredPheromone: Double = DEFAULT_PHEROMONE
    var pheromone: Double = unpoweredPheromone.pow(BETA)
    private set
    var addition: Double = 0.0
    val proximity = (1.0 / node1.calcDistance(node2)).pow(ALPHA)

    var wish = pheromone * proximity
    private set

    fun mergePheromones() {
        this.unpoweredPheromone =
            checkPheromone(unpoweredPheromone * (1 - DISAPPEARANCE) +
                    addition * DISAPPEARANCE)

        this.pheromone = unpoweredPheromone.pow(BETA)
        this.wish = pheromone * proximity
        addition = 0.0
    }

    private fun checkPheromone(pheromone: Double) = when {
        pheromone < MIN_PHEROMONE -> MIN_PHEROMONE
        (MAX_PHEROMONE != -1.0) && (pheromone > MAX_PHEROMONE) -> MAX_PHEROMONE
        else -> pheromone
    }

    override fun toString(): String {
        return "From <${node1.id}> to <${node2.id}>: " +
                "Unpowered Pheromone <$unpoweredPheromone> " +
                "Wish <$wish>"
    }

    override fun equals(other: Any?): Boolean {
        if (other is Edge) {
            return (node1 == other.node1 && node2 == other.node2)
                    || (node2 == other.node1 && node1 == other.node2)
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = node1.hashCode()
        result = 31 * result + node2.hashCode()
        result = 31 * result + proximity.hashCode()
        return result
    }
}