package edge

import settings.Constants
import settings.GlobalVariables
import node.Node
import node.NodesBuffer
import java.io.File

object EdgesBuffer {
    private val edges: Array<Array<Edge>> = Array(NodesBuffer.size) { i ->
        Array (NodesBuffer.size) { j ->
            Edge(NodesBuffer.get()[i], NodesBuffer.get()[j])
        }
    }

    fun decreasePheromone(node1: Node, node2: Node, percentageStay: Double) {
        edges[node1.id][node2.id].unpoweredPheromone *= percentageStay
        edges[node2.id][node1.id].unpoweredPheromone *= percentageStay
    }

    fun addPheromones(nodes: Array<Node>, pheromone: Double) {
        nodes.forEachIndexed { i, node ->
            if (i != nodes.lastIndex) {
                addPheromone(node, nodes[i + 1], pheromone)
            }
        }
    }

    private fun addPheromone(node1: Node, node2: Node, pheromone: Double) {
        val foundedEdge = edges[node1.id][node2.id]
        val foundedEdge2 = edges[node2.id][node1.id]
        foundedEdge.addition += pheromone
        foundedEdge2.addition += pheromone
    }

    fun getWish(node1: Node, node2: Node) : Double {
        return edges[node1.id][node2.id].wish
    }

    fun getPheromoneAndLen(node1: Node, node2: Node) : Array<Double> {
        val foundedEdge = edges[node1.id][node2.id]
        return arrayOf(foundedEdge.pheromone, foundedEdge.proximity)
    }

    fun printAllData() {
        File("Edges.txt").printWriter().use { writer ->
            writer.println(GlobalVariables.generation)
            edges.forEach {
                it.forEach { edge ->
                    if (edge.unpoweredPheromone > Constants.MIN_PHEROMONE) {
                        writer.println(edge)
                    }
                }
            }
        }
    }

    fun mergePheromones() {
        edges.forEach {
            it.forEach { edge ->
                edge.mergePheromones()
            }
        }
    }
}