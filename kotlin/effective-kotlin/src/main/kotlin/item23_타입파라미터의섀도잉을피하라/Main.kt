package item23_타입파라미터의섀도잉을피하라

class Webtoon(val name: String) {
    fun addComic(name: String) {
        println(name)
    }
}

interface Novel
class ReturnOfTheMadDemon: Novel
class ADanceOfSwordsInTheNight: Novel

class Series<T: Novel> {
    fun <T: Novel> addNovel(novel: T) {
        println(novel)
    }

    fun addNovelReuseType(novel: T) {
        println(novel)
    }
}

fun main() {
//    val webtoon = Webtoon("여신강림")
//    webtoon.addComic("신의탑")

    val series = Series<ReturnOfTheMadDemon>()
    series.addNovel(ReturnOfTheMadDemon())
    series.addNovel(ADanceOfSwordsInTheNight())

    series.addNovelReuseType(ReturnOfTheMadDemon())
//    series.addNovelReuseType(ADanceOfSwordsInTheNight())
}