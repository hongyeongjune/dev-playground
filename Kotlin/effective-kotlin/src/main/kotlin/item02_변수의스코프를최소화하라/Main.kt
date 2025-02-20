package item02_변수의스코프를최소화하라

fun main() {
    example1()
    example2(10)
    example3()
    example4()
}

fun example1() {
    val webtoons = listOf<String>()

    // 나쁜 예
    var webtoon: String
    for (index in webtoons.indices) {
        webtoon = webtoons[index]
    }

    // 조금 더 좋은 예
    for (index in webtoons.indices) {
        var webtoon = webtoons[index]
    }

    // 제일 좋은 예
    for ((index, webtoon) in webtoons.withIndex()) { }
}

fun example2(degrees: Int) {
    val (color, description) = when {
        degrees < 5 -> "BLUE" to "cold"
        degrees < 23 -> "YELLOW" to "mild"
        else -> "RED" to "hot"
    }
}

fun example3() {
    val primes = sequence<Int> {
        var numbers = generateSequence(2) { it + 1 }

        while (true) {
            val prime = numbers.first()
            yield(prime)

            numbers = numbers.drop(1).filter { it % prime != 0 }
        }
    }

    println(primes.take(10).toList())
}

fun example4() {
    val primes = sequence<Int> {
        var numbers = generateSequence(2) { it + 1 }

        var prime: Int
        while (true) {
            prime = numbers.first()
            yield(prime)

            numbers = numbers.drop(1).filter { it % prime != 0 }
        }
    }

    println(primes.take(10).toList())

    print(listOf(1,2,3,4,5,6,7,8)
        .asSequence()
        .map {
            println("map : $it")
            it
        }

        .filter {
            println("filter : $it")
            it % 2 != 0
        }
        .filter {
            println("filter : $it")
            it % 2 != 0
        }
        .toList()
    )
}