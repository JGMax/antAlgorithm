package ant

import settings.Constants.AVAILABLE_NODES_NUMBER
import edge.EdgesBuffer
import node.Node
import node.NodesBuffer
import utills.roulette
import settings.GlobalVariables
import utills.Primes
import visualization.Chart

class Ant(
    val id: Int,
) {
    val path: ArrayList<Node> = arrayListOf()
    var pathLength = 0.0
        private set
    private lateinit var nodes: ArrayList<Node>
    var generation = 0
        private set
    private var startNode: Int = 0

    init {
        clear()
        if (id < 50) {
            visualize()
        }
    }

    fun run(): Array<Int> {
        while (nodes.isNotEmpty()) {
            val idx = roulette(calcProbabilities())
            println("Ant <$id> step: <${path.size}> generation: <$generation>")
            if (idx != -1) {
                addNodeToPath(idx)
            }
        }

        setCorrectPath()
        if (id < 50 && generation % 5 == 0) {
            visualize()
        }
        calcLen()
        return getPathIds()
    }

    private fun getPathIds(): Array<Int> {
        return Array(path.size) {
            path[it].id
        }
    }

    private fun calcLen() {
        pathLength = 0.0
        path.forEachIndexed { i, node ->
            if (i != path.lastIndex) {
                val distance = node.calcDistance(path[i + 1])
                pathLength += distance +
                        if (i % 10 == 0 && node.id !in Primes.primes) {
                            distance * 0.1
                        } else {
                            0.0
                        }
            }
        }
    }

    private fun setCorrectPath() {
        val zeroNode = path.indexOfFirst { it.id == 0 }
        val correctPath = MutableList(path.size) { path[(zeroNode + it) % path.size] }
        correctPath.forEachIndexed { i, node ->
            path[i] = node
        }
        setCycle()
    }

    private fun calcProbabilities(): Array<Double> {
        val probabilities = Array(nodes.size) { 0.0 }
        val wishes = Array(nodes.size) { 0.0 }
        val currentNode = path.last()

        var sumOfWishes = 0.0

        val availableNodes = getAvailableNodes(currentNode)
        availableNodes.forEachIndexed { i, node ->
            val wish = EdgesBuffer.getWish(currentNode, node)
            wishes[i] = wish
            sumOfWishes += wish
        }

        wishes.forEachIndexed { i, wish ->
            probabilities[i] = wish / sumOfWishes
        }

        return probabilities
    }

    private fun getAvailableNodes(currentNode: Node): ArrayList<Node> {
        return if (AVAILABLE_NODES_NUMBER != -1 && AVAILABLE_NODES_NUMBER < nodes.size) {
            val array = ArrayList<Node>()
            nodes.sortBy { currentNode.calcDistance(it) }
            for (i in 0 until AVAILABLE_NODES_NUMBER) {
                array.add(nodes[i])
            }
            array
        } else {
            nodes
        }
    }

    private fun visualize() {
        if (Chart.containsSeries(id.toString())) {
            Chart.updateData(id.toString(), getArrayForChart())
        } else {
            Chart.addSeries(id.toString(), getArrayForChart())
        }

    }

    private fun getArrayForChart(): Array<MutableList<Double>> {
        return Array(2) {
            if (it == 0) {
                MutableList(path.size) { idx ->
                    path[idx].x
                }
            } else {
                MutableList(path.size) { idx ->
                    path[idx].y
                }
            }
        }
    }

    private fun addNodeToPath(idx: Int) {
        path.add(nodes[idx])
        nodes.removeAt(idx)
    }

    private fun setCycle() {
        path.add(path[0])
    }

    fun clear() {
        path.clear()
        nodes = NodesBuffer.copy()

        if (GlobalVariables.availableStartNodes.isNotEmpty()) {
            startNode = GlobalVariables.availableStartNodes.random()
            GlobalVariables.availableStartNodes.remove(startNode)
        } else {
            startNode = NodesBuffer.get().random().id
        }

        addNodeToPath(startNode)

        if (id < 50 && generation % 5 == 0) {
            visualize()
        }
        generation++
    }

    fun copyPath() = Array(path.size) { path[it] }
}