package item01_가변성을제한하라

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

suspend fun main() {
    example1()
    example2()
    example3()
    example4()
    example5()
    example6()
}

fun example1() {
    var num = 0
    for (i in 1..1000) {
        thread {
            Thread.sleep(10L)
            num += 1
        }
    }
    Thread.sleep(1000L)
    println(num)
}

suspend fun example2() {
    var num = 0
    coroutineScope {
        for (i in 1..1000) {
            launch {
                delay(10L)
                num += 1
            }
        }
    }
    println(num)
}

fun example3() {
    val lock = Any()
    var num = 0
    for (i in 1..1000) {
        thread {
            Thread.sleep(10L)
            synchronized(lock) {
                num += 1
            }
        }
    }
    Thread.sleep(1000L)
    println(num)
    listOf(1,2,3).map {  }
}

data class User(
    val name: String,
    val surname: String,
)

fun example4() {
    val user1 = User("naver", "webtoon")
    val user2 = user1.copy(surname = "cloud")
    println(user2)
}

fun example5() {
    val list1 = mutableListOf<String>()
    var list2 = listOf<String>()
    list1.add("webtoon")
    list2 = list2 + "webtoon"
    println(list1)
    println(list2)
}

class UserRepository {
    private val storedUsers = mutableMapOf<Int, User>()

    fun findAll(): MutableMap<Int, User> {
        return storedUsers
    }
}

fun example6() {
    val userRepository = UserRepository()
    val users = userRepository.findAll()
    users[1] = User("naver", "webtoon") // {1=User(name=naver, surname=webtoon)}
    println(users)
}