package item05_예외를활용해코드에제한을걸어라

fun main() {
//    factorial(-2)
    example3()
}

fun factorial(num: Int): Long {
    require(num >= 0) {
        "Cannot calculate factorial of $num because it is smaller than 0"
    }
    return if (num <= 1) 1 else factorial(num -1) * num
}

fun isNull(text: String?) {
    checkNotNull(text)
}

fun example3() {
    val num = 3
    assert(num < 2)
    println("test")
}

data class Webtoon(
    val id: Int?,
    val subject: Any,
)

fun example4(webtoon: Webtoon) {
     require(webtoon.subject is String)
    // 스마트 캐스팅 -> require 함수가 없다면 type mismatch 발생
    val subject: String = webtoon.subject
}

fun example5(webtoon: Webtoon) {
    require(webtoon.id != null)
    // null 아님
    val id: Int = webtoon.id
}

fun example6(webtoon: Webtoon) {
    val id = webtoon.id ?: throw IllegalArgumentException("is null")
}

fun example7(webtoon: Webtoon) {
    val id = webtoon.id ?: run {
        println("Id is null")
        return
    }
}