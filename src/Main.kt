import java.io.File
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.max
import kotlin.math.min


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
    val horizontalSlides = convertHorizontalPhotosToSlides(horizontalIds)
    val slides = LinkedList<Slide>().let {
        it.addAll(horizontalSlides)
        it.addAll(verticalSlides)
        return@let it
    }

    printOutput(SlideShow(maximizeInterest(slides)))
}

fun convertHorizontalPhotosToSlides(horizontalPhotos: HashMap<Int, Photo>): LinkedList<Slide> {
    val horizontalSlides = LinkedList<Slide>()
    for(key in horizontalPhotos.keys)
    {
        val photo = horizontalPhotos[key] as Photo
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

fun maximizeInterest(slides: LinkedList<Slide>) : LinkedList<Slide>{
    val slideshow = LinkedList<Slide>()
    slideshow.add(slides.removeFirst())

    while (slides.isNotEmpty()){
        var maxHead = 0
        var maxTail = 0
        var headSlide = 0
        var tailSlide = 0
        for ((index, slide) in slides.withIndex()) {
            var compRet = compareSlides(slideshow.first, slide)
            if (maxHead < compRet) {
                maxHead = compRet
                headSlide = index
            }
            compRet = compareSlides(slideshow.last, slide)
            if (maxTail < compRet && slides.size != 1) {
                maxTail = compRet
                tailSlide = index
            }
        }

        if (maxHead > maxTail) {
            slideshow.addFirst(slides.removeAt(headSlide))
        } else {
            slideshow.addLast(slides.removeAt(tailSlide))
        }
    }

    return slideshow
}

fun compareSlides(s1: Slide, s2: Slide) : Int{
    val intersection = s1.tags.intersect(s2.tags)
    val leftTags = s1.tags.clone() as HashSet<String>
    val rightTags = s2.tags.clone() as HashSet<String>
    leftTags.removeAll(intersection)
    rightTags.removeAll(intersection)

    return min(leftTags.size, min(rightTags.size, intersection.size))
}
data class Photo(var id: Int, var orientation: Char, var tags: List<String>)
data class Slide(var tags: HashSet<String>, var photos: LinkedList<Photo>)
data class SlideShow(var slides: LinkedList<Slide>)