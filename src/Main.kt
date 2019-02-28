import java.io.File


fun main() {
    val lines = File("a.txt").readLines()

    val iterator = lines.iterator()

    val firstLine = iterator.next()

    val args = firstLine.split(" ")

    for (line in iterator) {


    }
}
