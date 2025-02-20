package item04_inferred타입으로리턴하지말라

fun main() { }

open class Webtoon
class TrueBeauty: Webtoon()
class Kubera: Webtoon()

val DEFAULT_WEBTOON: Webtoon = TrueBeauty()

interface WebtoonFactory {
    fun produce() = DEFAULT_WEBTOON
}

fun example1() {
    // type mismatch
    var webtoon1 = TrueBeauty()
    // error
    // webtoon1 = Webtoon()

    // TrueBeauty 는 Webtoon 을 상속받고 있기 때문에 해당 코드는 가능 (타입추론)
    var trueBeauty = Webtoon()
    trueBeauty = TrueBeauty()

    // 타입 명시적으로 지정해주면 type mismatch 해결
    var webtoon2: Webtoon = Kubera()
    webtoon2 = Webtoon()

    // type mismatch
    // error
    // var kubera: Kubera = Webtoon()
}

class WebtoonFactoryImpl : WebtoonFactory {
    override fun produce(): Webtoon {
        return super.produce()
    }
}