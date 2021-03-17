package settings

import java.awt.Color

object Constants {
    const val DISAPPEARANCE: Double = 0.9

    const val GLOBAL_MIN_LINE_NAME: String = "GLOBAL"
    val GLOBAL_MIN_LINE_COLOR: Color = Color(0, 0, 0)

    const val LOCAL_MIN_LINE_NAME: String = "LOCAL"
    val LOCAL_MIN_LINE_COLOR: Color = Color(98, 148, 248)

    const val ALPHA: Double = 1.0
    const val BETA: Double = 1.45

    const val POPULATION: Int = 2000
    const val ELITE_POPULATION: Int = 0

    const val STOP_TIME_MILLIS: Int = -1 // -1 is logic
    const val STOP_PERCENTAGE: Double = 0.7

    const val MIN_PHEROMONE: Double = 0.0
    const val MAX_PHEROMONE: Double = -1.0 // -1 is infinite
    const val DEFAULT_PHEROMONE: Double = 1.0

    const val INPUT_FILE: String = "src/main/resources/Input/cities.csv"
    const val OUTPUT_FILE: String = "src/main/resources/Output/path.txt"

    const val PHEROMONE_ADD: Double = 2000.0
    const val AVAILABLE_NODES_NUMBER = -1 // -1 is ignore
}