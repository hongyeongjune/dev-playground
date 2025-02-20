### 불필요한 객체 생성을 피하라

* 객체 생성은 언제나 비용이 들어가므로, 불필요한 객체 생성을 피하는 것이 최적화 관점에서 좋다.
* 다양한 레벨에서 객체 생성을 피할 수 있는데, 예시는 아래와 같다.

```kotlin
fun main() {
    // JVM 에서는 하나의 가상 머신에서 동일한 문자를 처리하는 코드가 여러 개 있다면, 기존 문자열을 재사용 한다.
    val str1 = "Lorem ipsum dolor sit amet"
    val str2 = "Lorem ipsum dolor sit amet"
    println(str1 == str2) // true
    println(str1 === str2) // true
    
    // Integer 는 -128 ~ 127 범위에는 캐시를 사용해서 재사용한다. (Integer cache 개념)
    val integer1: Int? = 1
    val integer2: Int? = 1
    println(integer1 == integer2) // true
    println(integer1 === integer2) // true
    val integer3: Int? = 130
    val integer4: Int? = 130
    println(integer3 == integer4) // true
    println(integer3 === integer4) // false
}
```

### 객체 생성 비용은 항상 클까 ?

* 객체는 더 많은 용량을 차지한다.
  * JDK 64비트 기준 int 는 4바이트지만, Integer 는 16바이트이다. 추가로 이에 대한 레퍼런스로 8바이트가 더 필요하다. (5배 이상 차이)
* 요소가 캡슐화되어 있다면, 접근에 추가적인 함수 호출이 필요하다. 함수를 사용하는 처리는 굉장히 빨라서 큰 비용이 발생하진 않지만, 티끌 모아 태산이 되는 것처럼 수많은 객체를 처리하면 이 비용도 커진다.
* 객체는 생성되고, 메모리 영역에 할당되고, 이에 대한 레퍼런스를 만드는 등의 작업이 필요하다.

### 객체 선언

* 매 순간 객체를 생성하지 않고, 객체를 재사용하는 간단한 방법은 객체 선언을 사용하는 것 이다. (싱글톤)

```kotlin
sealed class LinkedList<T>

class Node<T>(
    val head: T,
    val tail: LinkedList<T>
) : LinkedList<T>()

class Empty<T> : LinkedList<T>()

fun main() {
    val linkedList1 = Node(1, Node(2, Node(3, Empty())))
    val linkedList2 = Node("A", Node("B", Node("C", Empty())))
}
```
* 위 구현은 LinkedList 를 만들 때 마다 Empty 인스턴스를 만들어야한다.
* Empty 인스턴스를 하나만 만들고, 다른 모든 리스트에서 활용할 수 있게 하는게 성능에 더 좋다.
* 하지만, Empty 를 재사용하게 하기에는 제네릭 타입 때문에 문제가 될 수 있다. -> Nothing 타입을 활용하면 해결할 수 있다.
* Nothing 타입은 모든 타입의 서브타입이다. LinkedList<Nothing> 은 리스트가 covariant 라면 (out 한정자), 모든 LinkedList 에서 서브타입이다. 리스트는 immutable 하고, 이 타입은 out 위치에서만 사용되므로, 문제가 없다.
* 해당 방법은 immutable sealed 클래스를 정의할 때 자주 사용된다. 만약 mutable 객체에 사용하면 공유 상태 관리와 관련된 버그를 검출하기 어려워서 좋지 않을 수 있다. 

```kotlin
sealed class LinkedList<out T>

class Node<T>(
    val head: T,
    val tail: LinkedList<T>
) : LinkedList<T>()

object Empty : LinkedList<Nothing>()

fun main() {
    val linkedList1 = Node(1, Node(2, Node(3, Empty)))
    val linkedList2 = Node("A", Node("B", Node("C", Empty)))
}
```

### 캐시를 활용하는 팩터리 함수

* 팩터리 함수는 캐시를 가질 수 있다. -> 항상 같은 객체를 리턴하게 할 수 있다.
* 코루틴의 Dispatchers.Default 는 쓰레드 풀을 가지고있고, 어떤 처리를 시작하라고 명령하면, 사용하고 있지 않은 쓰레드 하나를 사용해 명령을 수행한다.
* 데이터베이스도 커넥션 풀을 가지고있고, 어떤 처리 명령을 시작하면, 사용하고 있지 않은 커넥센 하나를 사용해 명령을 수행한다.
* kotlin 의 emptyList 는 팩터리 함수로 같은 객체를 항상 리턴한다.

```kotlin
// kotlin 의 emptyList 는 팩터리 함수로 같은 객체를 항상 리턴한다.
internal object EmptyList : List<Nothing>

fun <T> emptyList(): List<T> = EmptyList
```

* parameterized 팩터리 메서드도 캐싱을 활용할 수 있다. 모든 순수 함수는 캐싱을 활용할 수 있으며 이를 memoization 이라 한다.
* 다만, 캐시를 위한 Map 을 저장해야 하므로, 더 많은 메모리를 사용하게 돼서 메모리 문제로 크래시가 생길 수 있고, 이러면 메모리를 해제해주어야 한다.
* 메모리가 관리를 위해 사용할 수 있는 참조 타입은 WeakReference 와 SoftReference 가 있다.
* WeakReference 는 약한 참조를 나타내며, 참조하는 객체가 다른 곳에서 참조하지 않을 때 가비지 컬렉터(Garbage Collector, GC)의 대상이 돤다. 따라서 다른 참조가 이를 사용하지 않으면 곧바로 제거한다.
* SoftReference 는 소프트 참조를 나타내며 가비지 컬렉터가 값을 정리할 수도, 정리하지 않을 수도 있다. 일반적인 JVM 구현의 경우, 메모리가 부족해서 추가로 필요한 경우에만 정리를 하게 된다. 따라서 캐시를 만들 때는 SoftReference 를 사용하는 것이 좋다.

```kotlin
private val connections: MutableMap<String, Connection> = mutableMapOf()

fun getConnection(host: String) = connections.getOrPut(host) { createConnection(host) }

private val FIB_CACHE: MutableMap<Int, BigInteger> = mutableMapOf()

fun fib(n: Int): BigInteger = FIB_CACHE.getOrPut(n) {
    if (n <= 1) BigInteger.ONE else fib(n-1) + fib(n-2)
}
```

### 무거운 객체를 외부 스코프로 보내기

* 컬렉션 처리에서 이루어지는 무거운 연산은 컬렉션 처리 함수 내부에서 외부로 빼는 것이 좋다.

```kotlin
// AS-IS
fun <T: Comparable<T>> Iterable<T>.countMax(): Int {
    return count { it == this.max() }
}

// TO-BE
// 매번 최댓값을 찾지 않으므로 성능이 향상되었다.
fun <T: Comparable<T>> Iterable<T>.countMax(): Int {
    val max = this.max()
    return count { it == max }
}
```

```kotlin
// AS-IS
// matches 반복마다 매번 새로 Regex 객체를 만드는 것이다. Regex 패턴 컴파일은 복잡한 연산이므로 반복적으로 만드는 것은 좋지 않다.
fun String.isValidIpAddress(): Boolean {
	return this.matches("\\A(?:(?:25[0-5]|2[0-4][0-9]...\\z".toRegex())
}

// TO-BE
// 정규식 top-level 로 올려 향상시키기
private val IS_VALID_EMAIL_REGEX = "\\A(?:(?:25[0-5]|2[0-4][0-9]...\\z".toRegex()

fun String.isValidIpAddress(): Boolean {
    return matches(IS_VALID_EMAIL_REGEX)
}

// To-BE + lazy init
// 이 함수가 한파일에 다른 함수들과 함께있지만, 이것이 사용되지 않는 다면 이 객체를 생성하지 않길 원할때 지연 초기화를 할 수 있다.
private val IS_VALID_EMAIL_REGEX by lazy {
  "\\A(?:(?:25[0-5]|2[0-4][0-9]...\\z".toRegex()
}

fun String.isValidIpAddress(): Boolean = matches(IS_VALID_EMAIL_REGEX)
```

### 지연 초기화

* 무거운 클래스를 생성할 때 lazy 하게 만드는 것이 더 좋을 때가 있다.

```kotlin
class B
class A {
	val b by lazy { B() }
}
```

* 내부에 있는 인스턴스들을 지연 초기화하면, A 객체를 생성하는 과정을 가볍게 만들 수 있다.
* 다만, 메서드의 호출이 빨라야 하는경우에는 좋지 않을 수 있다. -> 메서드 호출이 굉장히 빨라야한다면, 첫 호출 시 객체 생성 때문에 속도가 매우 지연될 수 있어서 오히려 좋지 않을 수 있다.

### 기본 자료형 사용하기

* JVM 은 숫자와 문자와 같은 기본적인 요소를 나타내기 위한 기본 자료형(primitives)를 가지고 있다.
* Kotlin/JVM 컴파일러는 가능하면 내부적으로 기본 자료형을 사용하지만 Wrapped class 를 대신 사용해야하는 상황이 있다.
  * nullable 타입을 연산할 때 (primitives는 null이 될 수 없음) 
  * 타입을 제네릭으로서 사용할 때
* 매우 큰 컬렉션을 처리할 때 기본 자료형과 wrap 한 자료형의 성능 차이는 매우 크다.

