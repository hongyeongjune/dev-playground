### 인라인 클래스의 사용을 고려하라

* 하나의 값을 보유하는 객체도 inline 으로 만들 수 있다. (코틀린 1.3 부터 도입)
* 기본 생성자 프로퍼티가 하나인 클래스 앞에 inline 을 붙이면, 해당 객체르 사용하는 위치가 모두 해당 프로퍼티로 교체된다.
 
* inline 클래스는 타입만 맞다면, 값을 곧대로 넣는것이 가능하다.

```kotlin
inline class Name(private val value: String)

class Age(private val value: Int)

fun main() {
    val name: Name = Name("Hong")
    println(name)
    
    val age: Age = Age(30)
    println(age)
}
```

```java
public final class MainKt {
   public static final void main() {
       // name 은 String 으로 그대로 사용
      String name = Name.constructor-impl("Hong");
      Name var1 = Name.box-impl(name);
      System.out.println(var1);
      // age 는 Age 객체로 생성
      Age age = new Age(30);
      System.out.println(age);
   }
}
```

* inline 클래스의 메서드는 모두 정적으로 만들어진다.

```java
public final class Name {
    private final String value;

    public static final void introduce_impl/* $FF was: introduce-impl*/(String $this) {
        String var1 = "My name is " + $this;
        System.out.println(var1);
    }
}

public final class MainKt {
    public static final void main() {
        String name = Name.constructor-impl("Hong");
        Name.introduce-impl(name);
    }
}
```

* inline class 는 다른 자료형을 Wrapping 해서 새로운 자료형을 만들 때 많이 사용한다. (위 코드는 String 을 Name 으로 Wrapping) -> 이 때, 어떠한 오버헤드도 발생하지 않는다.

### 측정 단위를 표현할 때 

* 타이머 클래스를 만들었는데, 이 클래스는 특정 시간 후 파라미터로 받은 함수를 호출한다.

```kotlin
interface Timer {
    fun callAfter(time: Int, callback: () -> Unit)
}
```

* 위 코드의 문제점은 time 이 ms, s, min 중에서 어떤 단위인지 명확하게 알 수 없다.
* 이 때 가장 쉽게 해결할 수 있는 방법은 파라미터 이름에 측정 단위를 붙이는 것 이다.

```kotlin
interface Timer {
    fun callAfter(timeMills: Int, callback: () -> Unit)
}
```

* 하지만, 이 방법은 함수를 사용할 때, 프로퍼티 이름이 표시되지 않을 수 있으므로, 여전히 실수를 할 수 있다.
* 또한, 파라미터는 이름을 붙일 수 있지만 리턴값은 이름을 붙일 수 없다.

```kotlin
interface User {
    fun decideAboutTime(): Int
    fun wakeUp()
}

interface Timer {
    fun callAfter(time: Int, callback: () -> Unit)
}

fun setUpUserWakeUpTime(user: User, timer: Timer) {
    val time = user.decideAboutTime()
    timer.callAfter(time) {
        user.wakeUp()
    }
}
```

* 다음 해결방법으로는 함수 이름을 변경하는 것 이다. ex. decideAboutTime -> decideAboutTimeTimeMills
* 하지만 이러한 방법은 함수를 더 길게 만들고, 필요없는 정보까지도 전달할 수 있어서 좋지 않다.
* 더 좋은 해결 방법은 타입에 제한을 거는 것 이다. 제한을 걸면 제네릭 유형을 잘못 사용하는 문제를 줄일 수 있다.
* 이 때 코드를 효율적으로 하려면 인라인 클래스를 사용하면 된다.

```kotlin
inline class Minutes(val minutes: Int) {
    fun toMillis(): Millis = Millis(minutes * 60 * 1000)
}

inline class Millis(val milliseconds: Int)

interface User {
    fun decideAboutTime(): Minutes
    fun wakeUp()
}

interface Timer {
    fun callAfter(timeMillis: Millis, callback: () -> Unit)
}

fun setUpUserWakeUpTime(user: User, timer: Timer) {
    val time = user.decideAboutTime()
    timer.callAfter(time.toMillis()) {
        user.wakeUp()
    }
}
```

* 이렇게 하면, 올바른 타입을 사용하는 것이 강제된다.

### 타입 오용으로 발생하는 문제를 막을 때 

* MySQL 데이터베이스는 PK 를 사용해서 요소를 식별하는데, AUTO_INCREMENT 로 설정하면 단순한 숫자로 설정된다.

```kotlin
@Table(name = "grade")
@Entity
class Slang(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    var gradeId: Int = 0L,

    @Column(name = "student_id")
    val studentId: Int,
    
    @Column(name = "teacher_id", nullable = false)
    val teacherId: Int,
)
```

* 그런데 이 코드는 모든 id 가 Int 자료형이므로, 실수로 잘못된 값를 넣을 수 있다.
* 또한 어떠한 오류도 발생하지 않으므로 문제가 될 수 있다.
* 이 때, inline 클래스를 활용하여 Wrapping 하면 된다.

```kotlin
inline class StudentId(val studentId: Int)
inline class TeacherId(val teacherId: Int)

@Table(name = "grade")
@Entity
class Slang(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    var gradeId: Int = 0L,

    @Column(name = "student_id")
    val studentId: StudentId,

    @Column(name = "teacher_id", nullable = false)
    val teacherId: TeacherId,
)
```

* 이렇게 하면 ID 를 사용하는것이 굉장히 안전해지며, 컴파일할 때 타입이 Int 로 대체되므로 코드를 바꾸어도 별 문제가 되지 않는다.
* 이처럼 인라인 클래스를 사용하면 안전을 위해서 새로운 타입을 도입해도 추가적인 오버헤드가 발생하지 않는다.

### 인라인 클래스와 인터페이스

* 인라인 클래스도 다른 클래스와 마찬가지로 인터페이스를 구현할 수 있는데, 이럴 경우 class 가 inline 으로 동작하지 않는다. 따라서 inline 을 썻을 때 얻을 수 있는 장점이 없다.

### typealias

* typealias 를 사용하면, 타입에 새로운 이름을 붙여 줄 수 있다.

```kotlin
typealias Seconds = Int
typealias Millis = Int

fun getTime(): Millis = 10
fun setUpTimer(time: Millis) {}

fun main() {
    val seconds: Seconds = 10
    val millis: Millis = seconds // 컴파일 오류 발생 X
    setUpTimer(millis)
    
}
```

* Seconds 와 Millis 를 둘다 Int 로 선언하여서 이를 혼용하여 사용하여도 에러가 발생하지 않는다. 
* 오히려 Millis 라고 이름이 명확하게 붙어 있음으로, 안전할 것이라는 착각을 하게 된다.
* 인라인 클래스를 사용하면 비용과 안전 두 마리 토끼를 잡을 수 있다.