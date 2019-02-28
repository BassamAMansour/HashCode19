import java.io.File
import java.util.*
import kotlin.collections.HashMap


fun main(args: Array<String>) {
    val lines = File("a.txt").readLines().toTypedArray()

    val n = lines[0].toInt()

    val horizontalPhotos = LinkedList<Photo>()
    val verticalPhotos = LinkedList<Photo>()
    val photosHorizontalMap = HashMap<String, List<Photo>>()
    val photosVerticalMap = HashMap<String, List<Photo>>()

    repeat(n) {

        val photo = getPhotoFromLine(lines[it], it)


    }
}

fun getPhotoFromLine(line: String, id: Int): Photo {
    val photoArgs = line.split(" ")
    return Photo(id, photoArgs[0][0], photoArgs.subList(2, photoArgs.size))
}

data class Photo(var id: Int, var orientation: Char, var tags: List<String>)