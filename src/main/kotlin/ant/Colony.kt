package ant

import settings.Constants.ELITE_POPULATION
import settings.Constants.GLOBAL_MIN_LINE_COLOR
import settings.Constants.GLOBAL_MIN_LINE_NAME
import settings.Constants.LOCAL_MIN_LINE_COLOR
import settings.Constants.LOCAL_MIN_LINE_NAME
import settings.Constants.OUTPUT_FILE
import settings.Constants.PHEROMONE_ADD
import settings.Constants.POPULATION
import settings.Constants.STOP_PERCENTAGE
import settings.Constants.STOP_TIME_MILLIS
import data.DataManager
import edge.EdgesBuffer
import settings.GlobalVariables.time
import node.Node
import node.NodesBuffer
import settings.GlobalVariables
import kotlinx.coroutines.*
import visualization.Chart
import java.awt.Color
import kotlin.random.Random

class Colony {
    private val colony = Array(POPULATION) { Ant(it) }

    private var globalMinPath: Array<Node> = arrayOf()
    private var globalMinPathIds: Array<Int> = arrayOf()
    private var globalMinLen = -1.0
    private var samePathCount = 0.0

    private var localMinLen = -1.0
    private val localMinPaths: ArrayList<Array<Node>> = arrayListOf()

    init {
        time = System.currentTimeMillis()
    }

    //todo refactor
    fun findWay() {
        while (System.currentTimeMillis() - time < STOP_TIME_MILLIS
            || STOP_TIME_MILLIS == -1) {

            val jobs: MutableList<Job> = mutableListOf()
            for (ant in colony) {
                runBlocking {
                    jobs.add(GlobalScope.launch(Dispatchers.Default) {
                        val pathIds = ant.run()

                        addPheromone(ant.pathLength, ant.copyPath())
                        updateGlobalMin(ant, pathIds)
                        updateLocalMin(ant)

                        if (ant.pathLength == globalMinLen && pathIds.contentEquals(globalMinPathIds)) {
                            samePathCount++
                        }
                        println("ant.Ant <${ant.id}> finished")

                        delay(2)
                    })
                }
            }

            runBlocking {
                jobs.forEach { it.join() }
            }

            eliteAntsRun()

            visualize(getArrayForChart(localMinPaths.random()),
                LOCAL_MIN_LINE_NAME,
                LOCAL_MIN_LINE_COLOR)
            visualize(getArrayForChart(globalMinPath),
                GLOBAL_MIN_LINE_NAME,
                GLOBAL_MIN_LINE_COLOR)

            println("<<<<<<<<<<<<<<<Local min>>>>>>>>>>>>>>>")
            println("<<<<<<<<<<<<<<$localMinLen>>>>>>>>>>>>>>")
            println("${samePathCount / POPULATION} has the same path")
            Thread.sleep(100)

            EdgesBuffer.mergePheromones()
            EdgesBuffer.printAllData()

            if (samePathCount / POPULATION >= STOP_PERCENTAGE) {
                break
            }

            setDefaultValues()
        }
    }

    private fun addPheromone(len: Double, path: Array<Node>) {
        val pheromone = PHEROMONE_ADD / len
        EdgesBuffer.addPheromones(path, pheromone)
    }

    private fun setDefaultValues() {
        GlobalVariables.availableStartNodes = NodesBuffer.ids()
        localMinLen = -1.0
        localMinPaths.clear()
        samePathCount = 0.0

        for (ant in colony) {
            ant.clear()
            GlobalVariables.generation = ant.generation
        }
    }

    private fun eliteAntsRun() {
        for (j in 0 until ELITE_POPULATION) {
            if (Random.nextBoolean()) {
                addPheromone(localMinLen, localMinPaths.random())
            } else {
                addPheromone(globalMinLen, globalMinPath)
            }
        }
    }

    private fun updateLocalMin(ant: Ant) {
        if (localMinLen == -1.0 || ant.pathLength < localMinLen) {

            localMinPaths.clear()
            localMinLen = ant.pathLength
            localMinPaths.add(ant.copyPath())

        } else if (localMinLen == ant.pathLength ) {
            localMinPaths.add(ant.copyPath())
        }
    }

    private fun updateGlobalMin(ant: Ant, pathIds: Array<Int>) {
        if (globalMinLen == -1.0 || ant.pathLength < globalMinLen) {
            samePathCount = 0.0
            for (i in 0 until 10) {
                println("<<<<<<<<<<<<<<<GLOBAL UPDATED>>>>>>>>>>>>>>>")
                println(ant.pathLength)
            }
            globalMinPath = ant.copyPath()
            globalMinPathIds = pathIds
            globalMinLen = ant.pathLength
            DataManager.outputFile = OUTPUT_FILE
            DataManager.writeData(globalMinPathIds, globalMinLen, ant.generation)
        }
    }

    private fun visualize(data: Array<MutableList<Double>>, name: String, color: Color) {
        try {
            if (Chart.containsSeries(name)) {
                Chart.updateData(name, data)
            } else {
                Chart.addSeries(name, data, color, 2.5f)
            }
        } catch (e: Exception) {
            println("Fail of visualization")
        }
    }

    private fun getArrayForChart(path: Array<Node>): Array<MutableList<Double>> {
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
}