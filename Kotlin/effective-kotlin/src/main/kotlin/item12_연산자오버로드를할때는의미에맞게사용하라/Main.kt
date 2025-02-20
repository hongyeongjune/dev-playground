package item12_연산자오버로드를할때는의미에맞게사용하라

fun Int.factorial(): Int = (1..this).product()

fun Iterable<Int>.product(): Int = fold(1) { acc, i -> acc * i }

operator fun Int.not() = factorial()

fun main() {
    println(!6)

    val point1 = Point(10, 30)
    val point2 = Point(30, 50)

    println(point1 + point2)

    val hello = 3 * { print("Hello") }
    hello()
}

data class Point(
    val x: Int,
    val y: Int,
) {
    operator fun plus(right: Point): Point = Point(x + right.x, y + right.y)
}

operator fun Int.times(operation: () -> Unit): () -> Unit = { repeat(this) { operation() } }