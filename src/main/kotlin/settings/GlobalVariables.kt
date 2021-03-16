package settings

import node.NodesBuffer

object GlobalVariables {
    var time: Long = 0
    var generation: Int = 1
    var availableStartNodes: ArrayList<Int> = NodesBuffer.ids()
}