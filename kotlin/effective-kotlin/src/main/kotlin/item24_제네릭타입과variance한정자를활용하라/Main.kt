package item24_제네릭타입과variance한정자를활용하라

class Series<out T>

open class Comic
class TrueBeauty : Comic()

class Webtoon<in T>
class TowerOfGod : Comic()

class LineManga<T>

fun main() {
//    val any: LineManga<Any> = LineManga<Int>()

    val comic1: Comic = TrueBeauty()
//    val trueBeauty1: TrueBeauty = Comic()

    val comic2: Series<Comic> = Series<TrueBeauty>()
//    val trueBeauty2: Series<TrueBeauty> = Series<Comic>()

    val comic3: Comic = TowerOfGod()
//    val towerOfGod3: TowerOfGod = Comic()

//    val comic4: Webtoon<Comic> = Webtoon<TowerOfGod>()
    val towerOfGod4: Webtoon<TowerOfGod> = Webtoon<Comic>()

    val intToNumber: (Int) -> Number = { it.toDouble() }
    val numberToAny: (Number) -> Any = { it.toString() }
    val numberToNumber: (Number) -> Number = { it }
    val numberToInt: (Number) -> Int = { it.toInt() }
    val anyToNumber: (Any) -> Number = { it.hashCode() }
    val anyToAny: (Any) -> Any = { it }

    process(intToNumber)
    process(numberToAny)
    process(numberToNumber)
    process(numberToInt)
    process(anyToNumber)
    process(anyToAny)

//    val resp: Response<Any> = Response<Int>
}

fun process(transition: (Int) -> Any) {
    println(transition(10))
}

class SoccerTeam<out T> {
    private var team: T? = null
    fun get(): T = team ?: error("team not set")
//    fun set(team: T) {
//        this.team = team
//    }
}
open class EPL
class Liverpool: EPL()
class Tottenham: EPL()

fun copy(from: MutableList<out EPL>, to: MutableList<EPL>) {
    val test1: MutableList<out EPL> = mutableListOf<Liverpool>()
    val test2: MutableList<out EPL> = mutableListOf<Tottenham>()

//    from[0] = Liverpool()

    for (index in from.indices) {
        to[index] = from[index]
    }
}

class Response<T>