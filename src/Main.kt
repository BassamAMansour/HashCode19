import java.io.File
import java.util.*
import kotlin.collections.HashMap


fun main(args: Array<String>) {
    val lines = File("a.txt").readLines().toTypedArray()

    val n = lines[0].toInt()

    val horizontalIds = HashMap<Int, Photo>()
    val verticalIds = HashMap<Int, Photo>()
    val horizontalTags = HashMap<String, LinkedList<Photo>>()
    val verticalTags = HashMap<String, LinkedList<Photo>>()

    repeat(n) {

        val photo = getPhotoFromLine(lines[it + 1], it)

        when (photo.orientation) {
            'H' -> {
                horizontalIds[it] = photo
                addTagsToMap(horizontalTags, photo)
            }

            'V' -> {
                verticalIds[it] = photo
                addTagsToMap(verticalTags, photo)
            }
        }
    }

    val verticalSlides = getOptimalVerticalSlides(verticalIds, verticalTags)
}

fun getOptimalVerticalSlides(
    verticalIds: HashMap<Int, Photo>,
    verticalMap: HashMap<String, LinkedList<Photo>>
): LinkedList<Slide> {

    val verticalSlides = LinkedList<Slide>()






    return verticalSlides

}

fun printOutput(slideShow: SlideShow) {
    File("out.txt").writeText("${slideShow.slides.size}\n${convertSlideShowToText(slideShow.slides)}")
}

fun convertSlideShowToText(slides: LinkedList<Slide>): String {

    return slides.joinToString {
        if (it.photos.size > 1) {
            ("${it.photos[0].id} ${it.photos[1].id}\n")
        } else {
            "${it.photos[0].id}\n"
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