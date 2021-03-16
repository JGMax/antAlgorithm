import ant.Colony
import settings.Constants.INPUT_FILE
import data.DataManager
import utills.objectsWake

fun main() {
    DataManager.inputFile = INPUT_FILE
    DataManager.readData()
    val colony = Colony()
    objectsWake()

    colony.findWay()
}