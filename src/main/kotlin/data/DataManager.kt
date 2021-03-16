package data
import settings.GlobalVariables
import node.Node
import node.NodesBuffer
import java.io.File

object DataManager {
    lateinit var inputFile: String
    lateinit var outputFile: String

    fun readData(delimiters:String = ",", lines2Miss: Int = 1) {
        val lines = File(inputFile).readLines()
        NodesBuffer.set(
            Array(lines.size - lines2Miss) {
                val data = lines[it + lines2Miss].split(delimiters)
                val id = data[0].toInt()
                val x = data[1].toDouble()
                val y = data[2].toDouble()
                Node(id, x, y)
            }
        )
    }

    fun writeData(data: Array<Int>, len: Double = -1.0, generation: Int = -1) {
        File(outputFile).printWriter().use {
            it.println("Time: ${System.currentTimeMillis() - GlobalVariables.time}")
            if (generation != -1) {
                it.println("Generation: $generation")
            }
            if (len != -1.0) {
                it.println("Length: $len")
            }
            data.forEach { id -> it.println(id) }
        }
    }
}