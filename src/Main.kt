import java.io.File
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet


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

    val verticalSlides = getOptimalVerticalSlides(horizontalPhotos, horizontalMap, verticalPhotos, verticalMap)
    val horizontalSlides = convertHorizontalPhotosToSlides(horizontalPhotos)
}

fun convertHorizontalPhotosToSlides(horizontalPhotos: LinkedList<Photo>): LinkedList<Slide> {
    val horizontalSlides = LinkedList<Slide>()
    for(photo in horizontalPhotos)
    {
        val tags = HashSet<String>()
        for(tag in photo.tags)
            tags.add(tag)
        val list = LinkedList<Photo>().let {
            it.add(photo)
            return@let it
        }
        horizontalSlides.add(Slide(tags, list))
    }
    return horizontalSlides
}

fun getOptimalVerticalSlides(
    horizontalPhotos: LinkedList<Photo>,
    horizontalMap: HashMap<String, LinkedList<Photo>>,
    verticalPhotos: LinkedList<Photo>,
    verticalMap: HashMap<String, LinkedList<Photo>>
): LinkedList<Slide> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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