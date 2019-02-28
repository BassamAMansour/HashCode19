import java.io.File
import java.util.*
import kotlin.collections.HashMap


fun main(args: Array<String>) {
    val lines = File("a.txt").readLines().toTypedArray()

    val n = lines[0].toInt()

    val horizontalPhotos = LinkedList<Photo>()
    val verticalPhotos = LinkedList<Photo>()
    val horizontalMap = HashMap<String, LinkedList<Photo>>()
    val verticalMap = HashMap<String, LinkedList<Photo>>()

    repeat(n) {

        val photo = getPhotoFromLine(lines[it + 1], it)

        when (photo.orientation) {
            'H' -> {
                horizontalPhotos.add(photo)
                addTagsToMap(horizontalMap, photo)
            }

            'V' -> {
                verticalPhotos.add(photo)
                addTagsToMap(verticalMap, photo)
            }
        }
    }


}

fun addTagsToMap(map: HashMap<String, LinkedList<Photo>>, photo: Photo) {
    for (tag in photo.tags) {
        if (map.containsKey(tag)) {
            map[tag]!!.add(photo)
        } else {
            val tags = LinkedList<Photo>()
            tags.add(photo)
            map[tag] = tags
        }
    }
}

fun getPhotoFromLine(line: String, id: Int): Photo {
    val photoArgs = line.split(" ")
    return Photo(id, photoArgs[0][0], photoArgs.subList(2, photoArgs.size))
}

data class Photo(var id: Int, var orientation: Char, var tags: List<String>)
data class Slide(var tags: HashSet<String>, var photos: LinkedList<Photo>)
data class SlideShow(var slides: LinkedList<Slide>)