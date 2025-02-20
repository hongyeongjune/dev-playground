package item22_일반적인알고리즘을구현할때제너릭을사용하라

fun main() {
    val list1 = listOf<String>("TEST1", "TEST2")
    val list2 = listOf<Int>(1,2)
    subTypeLimit(list1)
    //subTypeLimit(list2)
}

fun joinToString(list: List<Any>) {
    println(list.joinToString(","))
}

fun addNumber(list: MutableList<Any>) {
    list.add(10)
    println(list)
}

fun <T : String> subTypeLimit(list: List<T>) {
    println(list)
}