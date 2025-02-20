package item08_적절하게null을처리하라

fun main() {
    example3()
}

data class Webtoon(
    val id: Int? = null,
    val subject: String
)

fun getWebtoon(): Webtoon? {
    return null
}

fun example1() {
    val webtoon: Webtoon? = getWebtoon()

    // 컴파일 오류
//    webtoon.subject

    // 안전 호출
    webtoon?.subject

    // 스마트 캐스팅
    if (webtoon != null) webtoon.subject

    // not-null assertion
    webtoon!!.subject
}

fun example2(webtoon: Webtoon) {
    requireNotNull(webtoon.id)
    checkNotNull(webtoon.id)

    val id = webtoon.id ?: throw IllegalArgumentException("illegal argument exception")
}

fun example3(vararg nums: Int): Int {
    println(nums.max()!!)
    return 1
}