package node

object NodesBuffer {
    private lateinit var buffer: Array<Node>
    var size: Int = 0
    private set

    fun set(buffer: Array<Node>) {
        NodesBuffer.buffer = buffer
        size = NodesBuffer.buffer.size
    }

    fun get() = buffer

    fun copy() =
        ArrayList<Node>(buffer.toMutableList())

    fun ids() : ArrayList<Int> {
        val list = MutableList(buffer.size) { buffer[it].id }
        return ArrayList(list)
    }
}