package effective.item03_최대한플랫폼타입을사용하지말라

import item03_최대한플랫폼타입을사용하지말라.JavaClass

fun main() {
    example1()
    example2()
}

fun example1() {
    // 여기서 NPE 발생
    val value: String = JavaClass().value

    println(value.length)
}

fun example2() {
    // 플랫폼 타입
    val value = JavaClass().value

    // 여기서 NPE 발생
    println(value.length)
}