import java.io.File
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.min


fun main(args: Array<String>) {
    val lines = File("e.txt").readLines().toTypedArray()

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

    val verticalSlides = getOptimalVerticalSlides(verticalIds)

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
    for (key in horizontalPhotos.keys) {
        val photo = horizontalPhotos[key] as Photo
        val tags = HashSet<String>()
        for (tag in photo.tags)
            tags.add(tag)
        val list = LinkedList<Photo>().let {
            it.add(photo)
            return@let it
        }
        horizontalSlides.add(Slide(tags, list))
    }
    return horizontalSlides
}

fun getOptimalVerticalSlides(verticalIds: HashMap<Int, Photo>): LinkedList<Slide> {

    val verticalSlides = LinkedList<Slide>()

    val ids = verticalIds.keys
    val usedIds = HashSet<Int>()

    for (outerId in ids) {

        if (!usedIds.contains(outerId)) {

            val outerPhoto = verticalIds[outerId]!!

            var leastIntersection: Int = Int.MAX_VALUE
            var leastIntersectionId: Int = -1
            var leastInnerPhoto: Photo? = null

            for (innerId in ids) {
                if (!usedIds.contains(innerId) && innerId != outerId) {
                    val innerPhoto = verticalIds[innerId]!!
                    val intersection = outerPhoto.tags.intersect(innerPhoto.tags).size

                    if (intersection < leastIntersection) {
                        leastIntersectionId = innerId
                        leastIntersection = intersection
                        leastInnerPhoto = innerPhoto
                    }

                    if (leastIntersection <= 1) break
                }
            }

            if (leastIntersectionId != -1) {

                val photos = LinkedList<Photo>().apply {
                    add(outerPhoto)
                    add(leastInnerPhoto!!)
                }

                val tags = HashSet<String>()

                tags.addAll(outerPhoto.tags)
                tags.addAll(leastInnerPhoto!!.tags)

                verticalSlides.add(Slide(tags, photos))

                usedIds.addAll(listOf(outerId, leastIntersectionId))
            }

        }

    }

    return verticalSlides

}

fun printOutput(slideShow: SlideShow) {
    File("out.txt").writeText("${slideShow.slides.size}\n${convertSlideShowToText(slideShow.slides)}")
}

fun convertSlideShowToText(slides: LinkedList<Slide>): String {

    return slides.joinToString("\n") {
        if (it.photos.size > 1) {
            ("${it.photos[0].id} ${it.photos[1].id}")
        } else {
            "${it.photos[0].id}"
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

fun maximizeInterest(slides: LinkedList<Slide>): LinkedList<Slide> {
    val slideshow = LinkedList<Slide>()
    slideshow.add(slides.removeFirst())

    while (slides.isNotEmpty()) {
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

            if (maxHead <= 10) {
                break
            }

            compRet = compareSlides(slideshow.last, slide)
            if (maxTail < compRet && slides.size != 1) {
                maxTail = compRet
                tailSlide = index
            }

            if (maxTail <= 10) {
                break
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

fun compareSlides(s1: Slide, s2: Slide): Int {
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